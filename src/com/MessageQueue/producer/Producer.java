package com.MessageQueue.producer;

import com.MessageQueue.broker.interfaces.Broker;
import com.MessageQueue.model.Message;

import java.util.Map;
import java.util.UUID;

public class Producer implements Runnable {
    private final String id;
    private final Broker broker;
    private final Map<String, Object>[] messages;
    private final long ttlMs;

    @SafeVarargs
    public Producer(String id, Broker broker, long ttlMs, Map<String, Object>... messages) {
        this.id = id;
        this.broker = broker;
        this.messages = messages;
        this.ttlMs = ttlMs;
    }

    @SafeVarargs
    public Producer(String id, Broker broker, Map<String, Object>... messages) {
        this(id, broker, 0, messages);
    }

    @Override
    public void run() {
        for (Map<String, Object> payload : messages) {
            try {
                String msgId = id + "-" + UUID.randomUUID().toString().substring(0, 8);
                broker.publish(new Message(msgId, payload, ttlMs));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
