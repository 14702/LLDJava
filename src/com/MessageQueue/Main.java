package com.MessageQueue;

import com.MessageQueue.broker.impl.MessageBroker;
import com.MessageQueue.consumer.Consumer;
import com.MessageQueue.filter.MessageFilter;
import com.MessageQueue.producer.Producer;

import java.util.Arrays;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        MessageBroker broker = new MessageBroker(50, 3);

        // Consumer A: listens to all ORDER messages
        broker.registerConsumer(new Consumer("A", msg ->
                System.out.println("[A] Processed: " + msg),
                MessageFilter.keyEquals("type", "ORDER")));

        // Consumer B: listens to all messages
        broker.registerConsumer(new Consumer("B", msg ->
                System.out.println("[B] Processed: " + msg)));

        // Consumer C: depends on A and B, listens to ORDER messages
        broker.registerConsumer(new Consumer("C", msg ->
                System.out.println("[C] Processed (after A,B): " + msg),
                MessageFilter.keyEquals("type", "ORDER"),
                Arrays.asList("A", "B")));

        broker.start();

        // Multiple producers sending concurrently
        @SuppressWarnings("unchecked")
        Map<String, Object>[] orderPayloads = new Map[]{
                Map.of("type", "ORDER", "item", "laptop", "amount", 999),
                Map.of("type", "ORDER", "item", "phone", "amount", 499)
        };
        @SuppressWarnings("unchecked")
        Map<String, Object>[] paymentPayloads = new Map[]{
                Map.of("type", "PAYMENT", "method", "UPI", "amount", 999)
        };

        Thread p1 = new Thread(new Producer("producer-1", broker, orderPayloads));
        Thread p2 = new Thread(new Producer("producer-2", broker, paymentPayloads));
        p1.start();
        p2.start();
        p1.join();
        p2.join();

        Thread.sleep(500); // let consumers finish
        broker.stop();
        System.out.println("\nDone.");
    }
}
