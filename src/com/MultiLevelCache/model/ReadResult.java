package com.MultiLevelCache.model;

public class ReadResult {
    private final String value;
    private final int totalTime;

    public ReadResult(String value, int totalTime) {
        this.value = value;
        this.totalTime = totalTime;
    }

    public String getValue() { return value; }
    public int getTotalTime() { return totalTime; }
}
