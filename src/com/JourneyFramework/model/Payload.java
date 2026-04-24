package com.JourneyFramework.model;

import java.util.Map;

public class Payload {
    private final Map<String, String> data;

    public Payload(Map<String, String> data) {
        this.data = data;
    }

    public Map<String, String> getData() {
        return data;
    }
}
