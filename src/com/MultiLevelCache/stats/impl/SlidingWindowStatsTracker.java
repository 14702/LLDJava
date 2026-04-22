package com.MultiLevelCache.stats.impl;

import com.MultiLevelCache.stats.interfaces.StatsTracker;

import java.util.LinkedList;

public class SlidingWindowStatsTracker implements StatsTracker {

    private final int windowSize;
    private final LinkedList<Integer> readTimes = new LinkedList<>();
    private final LinkedList<Integer> writeTimes = new LinkedList<>();

    public SlidingWindowStatsTracker(int windowSize) {
        this.windowSize = windowSize;
    }

    @Override
    public synchronized void recordReadTime(int time) {
        readTimes.addLast(time);
        if (readTimes.size() > windowSize) readTimes.removeFirst();
    }

    @Override
    public synchronized void recordWriteTime(int time) {
        writeTimes.addLast(time);
        if (writeTimes.size() > windowSize) writeTimes.removeFirst();
    }

    @Override
    public synchronized double getAvgReadTime() {
        return avg(readTimes);
    }

    @Override
    public synchronized double getAvgWriteTime() {
        return avg(writeTimes);
    }

    public int getWindowSize() { return windowSize; }

    private double avg(LinkedList<Integer> list) {
        if (list.isEmpty()) return 0.0;
        return list.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }
}
