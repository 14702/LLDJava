package com.JourneyFramework.repository.impl;

import com.JourneyFramework.enums.ErrorCode;
import com.JourneyFramework.exception.JourneyException;
import com.JourneyFramework.model.Journey;
import com.JourneyFramework.repository.interfaces.JourneyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class JourneyRepositoryImpl implements JourneyRepository {
    private final ConcurrentHashMap<String, Journey> store = new ConcurrentHashMap<>();

    @Override
    public void save(Journey journey) {
        store.put(journey.getJourneyId(), journey);
    }

    @Override
    public Journey findById(String journeyId) {
        Journey j = store.get(journeyId);
        
        if (j == null) throw new JourneyException(ErrorCode.JOURNEY_NOT_FOUND, "Journey not found: " + journeyId);
        
        return j;
    }

    @Override
    public List<Journey> findAll() {
        return new ArrayList<>(store.values());}

    @Override
    public boolean exists(String journeyId) {
        return store.containsKey(journeyId);
    }
}
