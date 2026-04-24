package com.JourneyFramework.repository.interfaces;

import com.JourneyFramework.model.UserJourneyRecord;

public interface UserJourneyRepository {
    void save(UserJourneyRecord record);
    UserJourneyRecord findActive(String userId, String journeyId);
    boolean isOnboarded(String userId, String journeyId);
    int getRunCount(String userId, String journeyId);
}
