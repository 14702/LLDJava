package com.MultiLevelCache.factory;

import com.MultiLevelCache.eviction.interfaces.EvictionPolicy;
import com.MultiLevelCache.eviction.impl.LRUEvictionPolicy;
import com.MultiLevelCache.model.interfaces.CacheLevel;
import com.MultiLevelCache.model.impl.CacheLevelImpl;

public class CacheLevelFactory {

    public static CacheLevel create(int level, int capacity, int readTime, int writeTime) {
        return create(level, capacity, readTime, writeTime, new LRUEvictionPolicy());
    }

    public static CacheLevel create(int level, int capacity, int readTime, int writeTime, EvictionPolicy evictionPolicy) {
        return new CacheLevelImpl(level, capacity, readTime, writeTime, evictionPolicy);
    }
}
