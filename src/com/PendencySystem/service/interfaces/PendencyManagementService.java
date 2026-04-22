package com.PendencySystem.service.interfaces;

import java.util.List;

public interface PendencyManagementService {
    void startTracking(Integer id, List<String> hierarchicalTags);
    void stopTracking(Integer id);
    int getCounts(List<String> tags);
}
