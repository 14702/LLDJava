package com.JourneyFramework.model;

import java.util.Map;

public class Condition {
    private final Map<String, String> requiredParams;

    public Condition(Map<String, String> requiredParams) {
        this.requiredParams = requiredParams;
    }

    public boolean matches(Payload payload) {
        if (requiredParams == null || requiredParams.isEmpty()) return false;
        Map<String, String> data = payload.getData();
        if (data == null) return false;
        
        for (Map.Entry<String, String> entry : requiredParams.entrySet()) {
            if ( !entry.getValue().equals(data.get( entry.getKey() ) ) ) return false;
        }
        return true;
    }
}
