package com.JourneyFramework.observer.interfaces;

import com.JourneyFramework.model.Stage;

public interface StageTransitionObserver {
    void onTransition(String userId, String journeyId, Stage fromStage, Stage toStage);
}
