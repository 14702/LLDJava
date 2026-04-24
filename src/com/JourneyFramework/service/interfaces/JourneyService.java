package com.JourneyFramework.service.interfaces;

import com.JourneyFramework.enums.JourneyState;
import com.JourneyFramework.model.Journey;
import com.JourneyFramework.model.Payload;
import com.JourneyFramework.model.Stage;
import com.JourneyFramework.observer.interfaces.StageTransitionObserver;

public interface JourneyService {
    void createJourney(Journey journey);
    void updateState(String journeyId, JourneyState state);
    Journey getJourney(String journeyId);
    void evaluate(String userId, Payload payload);
    Stage getCurrentStage(String userId, String journeyId);
    boolean isOnboarded(String userId, String journeyId);
    void addObserver(StageTransitionObserver observer);
}
