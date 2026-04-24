package com.JourneyFramework.model;

import com.JourneyFramework.enums.JourneyState;
import com.JourneyFramework.enums.JourneyType;
import com.JourneyFramework.enums.StageType;

import java.util.*;

public class Journey {

    private final String journeyId;
    private final String name;
    private final JourneyType type;
    private JourneyState state;
    private final Date startDate;
    private final Date endDate;
    private final boolean recurring;

    private final Map<String, Stage> stages = new LinkedHashMap<>();
    private final List<Transition> transitions = new ArrayList<>();
    
    private Condition onboardingCondition;
    private String onboardingStageId;

    public Journey(String journeyId, String name, JourneyType type, Date startDate, Date endDate) {

        this(journeyId, name, type, startDate, endDate, false);
    }

    public Journey(String journeyId, String name, JourneyType type, Date startDate, Date endDate, boolean recurring) {

        this.journeyId = journeyId;
        this.name = name;
        this.type = type;
        this.state = JourneyState.CREATED;
        this.startDate = startDate;
        this.endDate = endDate;
        this.recurring = recurring;
    }

    public void addStage(Stage stage) {
        stages.put(stage.getStageId(), stage);

        if (stage.getType() == StageType.ONBOARDING) {
            this.onboardingStageId = stage.getStageId();
        }
    }

    public void addTransition(Transition transition) {
        transitions.add(transition);}

    public void setOnboardingCondition(Condition condition) {
        this.onboardingCondition = condition;
    }

    public boolean isExpired() {
        return type == JourneyType.TIME_BOUND && endDate != null && new Date().after(endDate);
    }

    public boolean hasStarted() {
        return startDate == null || new Date().after(startDate);
    }

    public Stage getStage(String stageId) {
        return stages.get(stageId);}

    public String getJourneyId() {
        return journeyId;}

    public String getName() {
        return name;
    }

    public JourneyType getType() {
        return type;
    }

    public JourneyState getState() {
        return state;
    }

    public void setState(JourneyState state) {
        this.state = state;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public Map<String, Stage> getStages() {
        return stages;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public Condition getOnboardingCondition() {
        return onboardingCondition;
    }

    public String getOnboardingStageId() {
        return onboardingStageId;
    }
}
