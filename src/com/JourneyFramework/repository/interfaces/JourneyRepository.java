package com.JourneyFramework.repository.interfaces;

import com.JourneyFramework.model.Journey;
import java.util.List;

public interface JourneyRepository {
    void save(Journey journey);
    Journey findById(String journeyId);
    List<Journey> findAll();
    boolean exists(String journeyId);
}
