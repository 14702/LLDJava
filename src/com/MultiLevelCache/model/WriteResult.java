package com.MultiLevelCache.model;

public class WriteResult {
    private final int totalTime;

    public WriteResult(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getTotalTime() { return totalTime; }
}
