package com.PendencySystem.repository.interfaces;

import java.util.List;

public interface TagRepository {
    void incrementTags(List<String> tags);
    void decrementTags(List<String> tags);
    int getCounts(List<String> tags);
}
