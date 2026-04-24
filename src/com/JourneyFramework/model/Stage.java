package com.JourneyFramework.model;

import com.JourneyFramework.enums.StageType;

public class Stage {
    private final String stageId;
    private final String name;
    private final StageType type;
    private final boolean notifyOnEntry;

    public Stage(String stageId, String name, StageType type) {
        this(stageId, name, type, false);
    }

    public Stage(String stageId, String name, StageType type, boolean notifyOnEntry) {
        this.stageId = stageId;
        this.name = name;
        this.type = type;
        this.notifyOnEntry = notifyOnEntry;
    }

    public String getStageId() {
        return stageId;
    }

    public String getName() {
        return name;
    }

    public StageType getType() {
        return type;
    }

    public boolean isNotifyOnEntry() {
        return notifyOnEntry;
    }

    @Override
    public String toString() {
        return name + "(" + type + ")";
    }
}
