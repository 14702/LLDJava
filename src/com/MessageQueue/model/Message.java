package com.MessageQueue.model;

import java.util.Map;

public class Message {
    private final String id;
    private final Map<String, Object> payload;
    private final long createdAt;
    private final long ttlMs; // 0 means no expiry

    public Message(String id, Map<String, Object> payload, long ttlMs) {
        if (id == null || payload == null) throw new IllegalArgumentException("id and payload required");
        this.id = id;
        this.payload = payload;
        this.createdAt = System.currentTimeMillis();
        this.ttlMs = ttlMs;
    }

    public Message(String id, Map<String, Object> payload) {
        this(id, payload, 0);
    }

    public String getId() { return id; }
    public Map<String, Object> getPayload() { return payload; }
    public long getCreatedAt() { return createdAt; }

    public boolean isExpired() {
        return ttlMs > 0 && (System.currentTimeMillis() - createdAt) > ttlMs;
    }

    @Override
    public String toString() {
        return "Message{id='" + id + "', payload=" + payload + "}";
    }
}
