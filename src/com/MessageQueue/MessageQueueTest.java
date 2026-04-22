package com.MessageQueue;

import com.MessageQueue.broker.impl.MessageBroker;
import com.MessageQueue.consumer.Consumer;
import com.MessageQueue.filter.MessageFilter;
import com.MessageQueue.model.Message;
import com.MessageQueue.producer.Producer;
import com.MessageQueue.queue.impl.BoundedQueue;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageQueueTest {

    private static int passed = 0, failed = 0;

    public static void main(String[] args) throws Exception {
        testBoundedQueueBasic();
        testBoundedQueueBoundsEnforced();
        testMessageTTL();
        testFilterKeyEquals();
        testFilterKeyContains();
        testSingleProducerMultipleConsumers();
        testConsumerDependencyOrdering();
        testRetryOnFailure();
        testTTLExpiredMessageNotDelivered();
        testConcurrentProducers();
        testExpressionBasedSubscription();

        System.out.println("\n=== Results: " + passed + " passed, " + failed + " failed ===");
    }

    static void testBoundedQueueBasic() throws Exception {
        BoundedQueue q = new BoundedQueue(5);
        q.put(new Message("1", Map.of("k", "v")));
        q.put(new Message("2", Map.of("k", "v2")));
        assertEqual("queue size after 2 puts", 2, q.size());
        Message m = q.take();
        assertEqual("FIFO order", "1", m.getId());
        assertEqual("queue size after take", 1, q.size());
    }

    static void testBoundedQueueBoundsEnforced() {
        BoundedQueue q = new BoundedQueue(2);
        assertTrue("offer 1", q.offer(new Message("1", Map.of("a", 1))));
        assertTrue("offer 2", q.offer(new Message("2", Map.of("a", 2))));
        assertFalse("offer 3 rejected (full)", q.offer(new Message("3", Map.of("a", 3))));
        assertEqual("size is capacity", 2, q.size());
    }

    static void testMessageTTL() throws Exception {
        Message m = new Message("ttl", Map.of("x", 1), 50);
        assertFalse("not expired immediately", m.isExpired());
        Thread.sleep(80);
        assertTrue("expired after TTL", m.isExpired());
    }

    static void testFilterKeyEquals() {
        MessageFilter f = MessageFilter.keyEquals("type", "ORDER");
        assertTrue("matches ORDER", f.matches(new Message("1", Map.of("type", "ORDER"))));
        assertFalse("rejects PAYMENT", f.matches(new Message("2", Map.of("type", "PAYMENT"))));
    }

    static void testFilterKeyContains() {
        MessageFilter f = MessageFilter.keyContains("body", "error");
        assertTrue("contains error", f.matches(new Message("1", Map.of("body", "an error occurred"))));
        assertFalse("no match", f.matches(new Message("2", Map.of("body", "all good"))));
    }

    static void testSingleProducerMultipleConsumers() throws Exception {
        MessageBroker broker = new MessageBroker(10);
        CountDownLatch latch = new CountDownLatch(3); // 3 consumers x 1 message each
        AtomicInteger totalReceived = new AtomicInteger(0);

        for (String id : new String[]{"A", "B", "C"}) {
            broker.registerConsumer(new Consumer(id, msg -> {
                totalReceived.incrementAndGet();
                latch.countDown();
            }));
        }
        broker.start();
        broker.publish(new Message("m1", Map.of("data", "hello")));
        latch.await(2, TimeUnit.SECONDS);
        broker.stop();

        assertEqual("all 3 consumers received", 3, totalReceived.get());
    }

    static void testConsumerDependencyOrdering() throws Exception {
        MessageBroker broker = new MessageBroker(10);
        List<String> order = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch done = new CountDownLatch(3);

        broker.registerConsumer(new Consumer("A", msg -> {
            Thread.sleep(50); // simulate work
            order.add("A");
            done.countDown();
        }));
        broker.registerConsumer(new Consumer("B", msg -> {
            order.add("B");
            done.countDown();
        }));
        // C depends on A and B
        broker.registerConsumer(new Consumer("C", msg -> {
            order.add("C");
            done.countDown();
        }, MessageFilter.acceptAll(), Arrays.asList("A", "B")));

        broker.start();
        broker.publish(new Message("m1", Map.of("v", 1)));
        done.await(3, TimeUnit.SECONDS);
        broker.stop();

        int idxA = order.indexOf("A");
        int idxB = order.indexOf("B");
        int idxC = order.indexOf("C");
        assertTrue("C after A", idxC > idxA);
        assertTrue("C after B", idxC > idxB);
    }

    static void testRetryOnFailure() throws Exception {
        MessageBroker broker = new MessageBroker(10, 3);
        AtomicInteger attempts = new AtomicInteger(0);
        CountDownLatch done = new CountDownLatch(1);

        broker.registerConsumer(new Consumer("retry-consumer", msg -> {
            if (attempts.incrementAndGet() < 3) throw new RuntimeException("transient failure");
            done.countDown();
        }));
        broker.start();
        broker.publish(new Message("m1", Map.of("x", 1)));
        done.await(2, TimeUnit.SECONDS);
        broker.stop();

        assertEqual("retried 3 times", 3, attempts.get());
    }

    static void testTTLExpiredMessageNotDelivered() throws Exception {
        MessageBroker broker = new MessageBroker(10);
        AtomicInteger received = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(1);

        broker.registerConsumer(new Consumer("ttl-consumer", msg -> {
            received.incrementAndGet();
            latch.countDown();
        }));

        // Publish a message with very short TTL, then a normal one
        Message shortLived = new Message("expire", Map.of("a", 1), 1);
        Thread.sleep(10); // let it expire
        broker.publish(shortLived);
        broker.publish(new Message("alive", Map.of("a", 2)));

        broker.start();
        latch.await(2, TimeUnit.SECONDS);
        Thread.sleep(100); // give time for expired to be skipped
        broker.stop();

        assertEqual("only non-expired delivered", 1, received.get());
    }

    static void testConcurrentProducers() throws Exception {
        MessageBroker broker = new MessageBroker(100);
        AtomicInteger received = new AtomicInteger(0);
        int producerCount = 5, msgsPerProducer = 10;
        int total = producerCount * msgsPerProducer;
        CountDownLatch latch = new CountDownLatch(total);

        broker.registerConsumer(new Consumer("counter", msg -> {
            received.incrementAndGet();
            latch.countDown();
        }));
        broker.start();

        List<Thread> producers = new ArrayList<>();
        for (int p = 0; p < producerCount; p++) {
            @SuppressWarnings("unchecked")
            Map<String, Object>[] payloads = new Map[msgsPerProducer];
            for (int i = 0; i < msgsPerProducer; i++) {
                payloads[i] = Map.of("idx", p * msgsPerProducer + i);
            }
            Thread t = new Thread(new Producer("P" + p, broker, payloads));
            producers.add(t);
            t.start();
        }
        for (Thread t : producers) t.join();

        latch.await(3, TimeUnit.SECONDS);
        broker.stop();

        assertEqual("all messages from concurrent producers", total, received.get());
    }

    static void testExpressionBasedSubscription() throws Exception {
        MessageBroker broker = new MessageBroker(10);
        AtomicInteger orderCount = new AtomicInteger(0);
        AtomicInteger paymentCount = new AtomicInteger(0);
        CountDownLatch done = new CountDownLatch(3); // 2 orders + 1 payment matched

        broker.registerConsumer(new Consumer("order-listener", msg -> {
            orderCount.incrementAndGet();
            done.countDown();
        }, MessageFilter.keyEquals("type", "ORDER")));

        broker.registerConsumer(new Consumer("payment-listener", msg -> {
            paymentCount.incrementAndGet();
            done.countDown();
        }, MessageFilter.keyEquals("type", "PAYMENT")));

        broker.start();
        broker.publish(new Message("1", Map.of("type", "ORDER", "amount", 100)));
        broker.publish(new Message("2", Map.of("type", "PAYMENT", "amount", 200)));
        broker.publish(new Message("3", Map.of("type", "ORDER", "amount", 300)));
        done.await(2, TimeUnit.SECONDS);
        broker.stop();

        assertEqual("order consumer got 2", 2, orderCount.get());
        assertEqual("payment consumer got 1", 1, paymentCount.get());
    }

    // --- Assertion Helpers ---

    static void assertEqual(String name, Object expected, Object actual) {
        if (Objects.equals(expected, actual)) { passed++; System.out.println("  PASS: " + name); }
        else { failed++; System.out.println("  FAIL: " + name + " (expected=" + expected + ", actual=" + actual + ")"); }
    }

    static void assertTrue(String name, boolean cond) {
        if (cond) { passed++; System.out.println("  PASS: " + name); }
        else { failed++; System.out.println("  FAIL: " + name + " (expected true)"); }
    }

    static void assertFalse(String name, boolean cond) {
        if (!cond) { passed++; System.out.println("  PASS: " + name); }
        else { failed++; System.out.println("  FAIL: " + name + " (expected false)"); }
    }
}
