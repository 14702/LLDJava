package com.MultiLevelCache.service.impl;

import com.MultiLevelCache.model.interfaces.CacheLevel;
import com.MultiLevelCache.model.ReadResult;
import com.MultiLevelCache.model.WriteResult;
import com.MultiLevelCache.service.interfaces.CacheService;
import com.MultiLevelCache.stats.interfaces.StatsTracker;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MultiLevelCacheService implements CacheService {

    private final CopyOnWriteArrayList<CacheLevel> levels;
    private final StatsTracker statsTracker;

    public MultiLevelCacheService(List<CacheLevel> levels, StatsTracker statsTracker) {
        this.levels = new CopyOnWriteArrayList<>(levels);
        this.statsTracker = statsTracker;
    }

    @Override
    public ReadResult read(String key) {
        int totalTime = 0;
        String value = null;
        int foundAt = -1;

        for (int i = 0; i < levels.size(); i++) {
            CacheLevel level = levels.get(i);
            totalTime += level.getReadTime();
            value = level.read(key);
            if (value != null) {
                foundAt = i;
                break;
            }
        }

        if (value != null && foundAt > 0) {
            for (int i = 0; i < foundAt; i++) {
                totalTime += levels.get(i).getWriteTime();
                levels.get(i).write(key, value);
            }
        }

        statsTracker.recordReadTime(totalTime);
        return new ReadResult(value, totalTime);
    }

    @Override
    public WriteResult write(String key, String value) {
        int totalTime = 0;

        for (CacheLevel level : levels) {
            totalTime += level.getReadTime();
            String existing = level.read(key);
            if (existing != null && existing.equals(value)) {
                break;
            }
            totalTime += level.getWriteTime();
            level.write(key, value);
        }

        statsTracker.recordWriteTime(totalTime);
        return new WriteResult(totalTime);
    }

    @Override
    public void addLevel(CacheLevel level) {
        levels.add(level);
    }

    @Override
    public void removeLevel(int levelNumber) {
        levels.removeIf(l -> l.getLevel() == levelNumber);
    }

    @Override
    public void stat() {
        System.out.println("--- Cache Stats ---");
        System.out.println("  Levels: " + levels.size());
        for (CacheLevel level : levels) {
            System.out.println("  L" + level.getLevel() + " : " + level.getUsage() + " / " + level.getCapacity());
        }
        System.out.printf("  Avg Read Time  : %.2f%n", statsTracker.getAvgReadTime());
        System.out.printf("  Avg Write Time : %.2f%n", statsTracker.getAvgWriteTime());
        System.out.println("-------------------");
    }
}
