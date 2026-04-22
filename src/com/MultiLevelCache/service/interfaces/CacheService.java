package com.MultiLevelCache.service.interfaces;

import com.MultiLevelCache.model.interfaces.CacheLevel;
import com.MultiLevelCache.model.ReadResult;
import com.MultiLevelCache.model.WriteResult;

public interface CacheService {
    ReadResult read(String key);
    WriteResult write(String key, String value);
    void addLevel(CacheLevel level);
    void removeLevel(int levelNumber);
    void stat();
}
