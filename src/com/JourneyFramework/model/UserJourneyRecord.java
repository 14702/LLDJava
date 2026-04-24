package com.JourneyFramework.model;

public class UserJourneyRecord {
    private final String userId;
    private final String journeyId;
    private String currentStageId;
    private final int runNumber;

    public UserJourneyRecord(String userId, String journeyId, String stageId, int runNumber) {
        this.userId = userId;
        this.journeyId = journeyId;
        this.currentStageId = stageId;
        this.runNumber = runNumber;
    }

    public void moveToStage(String stageId) {
        this.currentStageId = stageId;}

    public String getUserId() {
        return userId;
    }

    public String getJourneyId() {
        return journeyId;
    }

    public String getCurrentStageId() {
        return currentStageId;
    }

    public int getRunNumber() {
        return runNumber;
    }
}
