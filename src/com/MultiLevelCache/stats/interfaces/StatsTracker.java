package com.MultiLevelCache.stats.interfaces;

public interface StatsTracker {
    void recordReadTime(int time);
    void recordWriteTime(int time);
    double getAvgReadTime();
    double getAvgWriteTime();
}
