package com.MultiLevelCache.model.interfaces;

public interface CacheLevel {
    String read(String key);
    void write(String key, String value);
    int getLevel();
    int getCapacity();
    int getReadTime();
    int getWriteTime();
    int getUsage();
}
