package com.JourneyFramework.repository.impl;

import com.JourneyFramework.model.UserJourneyRecord;
import com.JourneyFramework.repository.interfaces.UserJourneyRepository;

import java.util.concurrent.CopyOnWriteArrayList;

public class UserJourneyRepositoryImpl implements UserJourneyRepository {
    private final CopyOnWriteArrayList<UserJourneyRecord> records = new CopyOnWriteArrayList<>();

    @Override
    public void save(UserJourneyRecord record) {
        records.add(record);
    }

    @Override
    public UserJourneyRecord findActive(String userId, String journeyId) {
        UserJourneyRecord latest = null;

        for (UserJourneyRecord r : records) {
            
            if (r.getUserId().equals(userId) && r.getJourneyId().equals(journeyId)) {
                if (latest == null || r.getRunNumber() > latest.getRunNumber()) {
                    latest = r;
                }

            }
        }
        return latest;
    }

    @Override
    public boolean isOnboarded(String userId, String journeyId) {
        return findActive(userId, journeyId) != null;
    }

    @Override
    public int getRunCount(String userId, String journeyId) {
        int count = 0;
        
        for (UserJourneyRecord r : records) {
            if (r.getUserId().equals(userId) && r.getJourneyId().equals(journeyId)) 
                count++;
        }

        return count;
    }
}
