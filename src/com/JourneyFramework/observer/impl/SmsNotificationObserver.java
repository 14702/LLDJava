package com.JourneyFramework.observer.impl;

import com.JourneyFramework.model.Stage;
import com.JourneyFramework.observer.interfaces.StageTransitionObserver;

public class SmsNotificationObserver implements StageTransitionObserver {
// only sends SMS if the target stage has notify on entry enabled

    @Override
    public void onTransition(String userId, String journeyId, Stage fromStage, Stage toStage) {

        if (toStage != null && toStage.isNotifyOnEntry()) {
            String from = (fromStage != null) ? fromStage.getName() : "NONE";

            System.out.println("--->>> Sending SMS :  user " + userId + " moved from " + from + " - > " + toStage.getName() + " in journey " + journeyId);
        
        }
    }
}
