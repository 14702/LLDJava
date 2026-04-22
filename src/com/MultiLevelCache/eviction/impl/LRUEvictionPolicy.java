package com.MultiLevelCache.eviction.impl;

import com.MultiLevelCache.eviction.interfaces.EvictionPolicy;
import java.util.LinkedHashSet;

public class LRUEvictionPolicy implements EvictionPolicy {

    private final LinkedHashSet<String> order = new LinkedHashSet<>();

    @Override
    public void keyAccessed(String key) {
        order.remove(key);
        order.add(key);
    }

    @Override
    public String evict() {
        if (order.isEmpty()) return null;
        String oldest = order.iterator().next();
        order.remove(oldest);
        return oldest;
    }

    @Override
    public void remove(String key) {
        order.remove(key);
    }
}
