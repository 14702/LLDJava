package com.MessageQueue.broker.impl;

import com.MessageQueue.broker.interfaces.Broker;
import com.MessageQueue.consumer.Consumer;
import com.MessageQueue.model.Message;
import com.MessageQueue.queue.impl.BoundedQueue;
import com.MessageQueue.queue.interfaces.Queue;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MessageBroker implements Broker {
    private final Queue queue;
    private final Map<String, Consumer> consumers = new ConcurrentHashMap<>();
    private final ExecutorService dispatchExecutor;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private Thread dispatchThread;
    private int maxRetries = 3;

    public MessageBroker(int queueCapacity) {
        this.queue = new BoundedQueue(queueCapacity);
        this.dispatchExecutor = Executors.newCachedThreadPool();
    }

    public MessageBroker(int queueCapacity, int maxRetries) {
        this(queueCapacity);
        this.maxRetries = maxRetries;
    }

    public Queue getQueue() { return queue; }

    public void registerConsumer(Consumer consumer) {
        for (String dep : consumer.getDependencies()) {
            if (!consumers.containsKey(dep))
                throw new IllegalArgumentException("Dependency '" + dep + "' not registered yet");
        }
        consumers.put(consumer.getId(), consumer);
    }

    public void unregisterConsumer(String consumerId) {
        consumers.remove(consumerId);
    }

    public void start() {
        if (running.compareAndSet(false, true)) {
            dispatchThread = new Thread(this::dispatchLoop, "broker-dispatch");
            dispatchThread.setDaemon(true);
            dispatchThread.start();
        }
    }

    public void stop() {
        running.set(false);
        if (dispatchThread != null) dispatchThread.interrupt();
        dispatchExecutor.shutdown();
    }

    private void dispatchLoop() {
        while (running.get()) {
            try {
                Message msg = queue.take();
                if (msg.isExpired()) continue;
                dispatch(msg);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void dispatch(Message msg) {
        Map<String, CountDownLatch> latches = new ConcurrentHashMap<>();
        for (Consumer c : consumers.values()) {
            latches.put(c.getId(), new CountDownLatch(1));
        }

        for (Consumer consumer : consumers.values()) {
            dispatchExecutor.submit(() -> {
                try {
                    waitForDependencies(consumer, latches);
                    if (msg.isExpired()) return;
                    if (!consumer.getFilter().matches(msg)) return;
                    invokeWithRetry(consumer, msg);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    CountDownLatch latch = latches.get(consumer.getId());
                    if (latch != null) latch.countDown();
                }
            });
        }
    }

    private void waitForDependencies(Consumer consumer, Map<String, CountDownLatch> latches)
            throws InterruptedException {
        for (String depId : consumer.getDependencies()) {
            CountDownLatch latch = latches.get(depId);
            if (latch != null) latch.await();
        }
    }

    private void invokeWithRetry(Consumer consumer, Message msg) {
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                consumer.getCallback().onMessage(msg);
                return;
            } catch (Exception e) {
                if (attempt == maxRetries) {
                    System.err.println("[Broker] Consumer '" + consumer.getId()
                            + "' failed after " + maxRetries + " retries: " + e.getMessage());
                }
            }
        }
    }

    public void publish(Message msg) throws InterruptedException {
        queue.put(msg);
    }

    public boolean tryPublish(Message msg) {
        return queue.offer(msg);
    }
}
