package com.MultiLevelCache.model.impl;

import com.MultiLevelCache.eviction.interfaces.EvictionPolicy;
import com.MultiLevelCache.model.interfaces.CacheLevel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class CacheLevelImpl implements CacheLevel {

    private final int level;
    private final int capacity;
    private final int readTime;
    private final int writeTime;
    private final ConcurrentHashMap<String, String> store;
    private final EvictionPolicy evictionPolicy;
    private final ReentrantLock lock = new ReentrantLock();

    public CacheLevelImpl(int level, int capacity, int readTime, int writeTime, EvictionPolicy evictionPolicy) {
        this.level = level;
        this.capacity = capacity;
        this.readTime = readTime;
        this.writeTime = writeTime;
        this.store = new ConcurrentHashMap<>();
        this.evictionPolicy = evictionPolicy;
    }

    @Override
    public String read(String key) {
        lock.lock();
        try {
            String val = store.get(key);
            if (val != null) {
                evictionPolicy.keyAccessed(key);
            }
            return val;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void write(String key, String value) {
        lock.lock();
        try {
            if (store.containsKey(key)) {
                store.put(key, value);
                evictionPolicy.keyAccessed(key);
                return;
            }
            if (store.size() >= capacity) {
                String evictedKey = evictionPolicy.evict();
                if (evictedKey != null) {
                    store.remove(evictedKey);
                }
            }
            store.put(key, value);
            evictionPolicy.keyAccessed(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int getLevel() { return level; }
    @Override
    public int getCapacity() { return capacity; }
    @Override
    public int getReadTime() { return readTime; }
    @Override
    public int getWriteTime() { return writeTime; }
    @Override
    public int getUsage() { return store.size(); }
}
