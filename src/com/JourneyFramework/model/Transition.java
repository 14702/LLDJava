package com.JourneyFramework.model;

public class Transition {
    private final String fromStageId;
    private final String toStageId;
    private final Condition condition;
    private final String actionName;

    public Transition(String fromStageId, String toStageId, Condition condition, String actionName) {
        this.fromStageId = fromStageId;
        this.toStageId = toStageId;
        this.condition = condition;
        this.actionName = actionName;
    }

    public String getFromStageId() {
        return fromStageId; }

    public String getToStageId() {
        return toStageId;
    }

    public Condition getCondition() {
        return condition;
    }
}
