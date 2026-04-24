package com.JourneyFramework.service.impl;

import com.JourneyFramework.enums.*;
import com.JourneyFramework.exception.JourneyException;
import com.JourneyFramework.model.*;
import com.JourneyFramework.observer.interfaces.StageTransitionObserver;
import com.JourneyFramework.repository.interfaces.JourneyRepository;
import com.JourneyFramework.repository.interfaces.UserJourneyRepository;
import com.JourneyFramework.service.interfaces.JourneyService;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class JourneyServiceImpl implements JourneyService {

    private final JourneyRepository journeyRepo;
    private final UserJourneyRepository userJourneyRepo;
    private final List<StageTransitionObserver> observers = new CopyOnWriteArrayList<>();

// state machine (not classes): CREATED -> ACTIVE <- > PAUSED (can resume), ACTIVE/PAUSED -> COMPLETED, ACTIVE -> EXPIRED (automaticlly at enddate)
    private static final Map<JourneyState, Set<JourneyState>> ALLOWED_TRANSITIONS = new HashMap<>();

    static {
        ALLOWED_TRANSITIONS.put(JourneyState.CREATED, new HashSet<>(Arrays.asList(JourneyState.ACTIVE)));
        ALLOWED_TRANSITIONS.put(JourneyState.ACTIVE, new HashSet<>(Arrays.asList(JourneyState.PAUSED, JourneyState.COMPLETED, JourneyState.EXPIRED)));
        ALLOWED_TRANSITIONS.put(JourneyState.PAUSED, new HashSet<>(Arrays.asList(JourneyState.ACTIVE, JourneyState.COMPLETED)));
        ALLOWED_TRANSITIONS.put(JourneyState.EXPIRED, Collections.emptySet());
        ALLOWED_TRANSITIONS.put(JourneyState.COMPLETED, Collections.emptySet());
    }

    public JourneyServiceImpl(JourneyRepository journeyRepo, UserJourneyRepository userJourneyRepo) {
        
        this.journeyRepo = journeyRepo;
        this.userJourneyRepo = userJourneyRepo;
    }

    @Override
    public void createJourney(Journey journey) {

        if (journey.getOnboardingStageId() == null) {
            throw new JourneyException(ErrorCode.INVALID_JOURNEY, "journey must have an ONBOARDING stage");
        }

        boolean hasTerminal = journey.getStages().values().stream().anyMatch(s -> s.getType() == StageType.TERMINAL);
        
        if (!hasTerminal) {
            throw new JourneyException(ErrorCode.INVALID_JOURNEY, "journey must have a TERMINAL stage");
        }

        if (journeyRepo.exists(journey.getJourneyId())) {
            throw new JourneyException(ErrorCode.JOURNEY_ALREADY_EXISTS, "journey already exists: " + journey.getJourneyId());
        }
        
        journey.setState(JourneyState.CREATED);
        journeyRepo.save(journey);
    }

    @Override
    public void updateState(String journeyId, JourneyState newState) {
        
        Journey journey = journeyRepo.findById(journeyId);
        expireIfNeeded(journey);

        Set<JourneyState> allowed = ALLOWED_TRANSITIONS.getOrDefault(journey.getState(), Collections.emptySet());
        
        if (!allowed.contains(newState)) {
            throw new JourneyException(ErrorCode.INVALID_STATE_TRANSITION, "cannot transition from " + journey.getState() + " to " + newState);
        }

        journey.setState(newState);
        journeyRepo.save(journey);
    }

    @Override
    public Journey getJourney(String journeyId) {

        Journey journey = journeyRepo.findById(journeyId);
        expireIfNeeded(journey);
        return journey;

    }

    @Override
    public void evaluate(String userId, Payload payload) {
        for (Journey journey : journeyRepo.findAll()) {
            expireIfNeeded(journey);
            if (journey.getState() != JourneyState.ACTIVE) continue;
            if (!journey.hasStarted()) continue;

            UserJourneyRecord record = userJourneyRepo.findActive(userId, journey.getJourneyId());

            boolean shouldOnboard = (record == null);

            if (!shouldOnboard && record != null) {
                Stage cur = journey.getStage(record.getCurrentStageId());

                if (cur != null && cur.getType() == StageType.TERMINAL && journey.isRecurring()) {
                    shouldOnboard = true;
                } else if (cur == null || cur.getType() != StageType.TERMINAL) {
                    tryTransition(userId, journey, record, payload);
                }
            }

            if (shouldOnboard) {
                Condition cond = journey.getOnboardingCondition();

                if (cond != null && cond.matches(payload)) {
                    int runNumber = userJourneyRepo.getRunCount(userId, journey.getJourneyId()) + 1;
                    UserJourneyRecord newRecord = new UserJourneyRecord(userId, journey.getJourneyId(), journey.getOnboardingStageId(), runNumber);
                    userJourneyRepo.save(newRecord);

                    Stage onboardingStage = journey.getStage(journey.getOnboardingStageId());

                    for (StageTransitionObserver obs : observers) {
                        obs.onTransition(userId, journey.getJourneyId(), null, onboardingStage);
                    }
                }
            }
        }
    }

    @Override
    public Stage getCurrentStage(String userId, String journeyId) {
        Journey journey = journeyRepo.findById(journeyId);
        UserJourneyRecord record = userJourneyRepo.findActive(userId, journeyId);

        if (record == null) {
            throw new JourneyException(ErrorCode.USER_NOT_ONBOARDED, "User " + userId + " not onboarded to journey " + journeyId);
        }

        return journey.getStage(record.getCurrentStageId());
    }

    @Override
    public boolean isOnboarded(String userId, String journeyId) {
        return userJourneyRepo.isOnboarded(userId, journeyId);
    }

    @Override
    public void addObserver(StageTransitionObserver observer) {
        observers.add(observer);
    }

    private void tryTransition(String userId, Journey journey, UserJourneyRecord record, Payload payload) {
        String currentStageId = record.getCurrentStageId();
        
        for (Transition t : journey.getTransitions()) {

            if (t.getFromStageId().equals(currentStageId) && t.getCondition().matches(payload)) {
                Stage fromStage = journey.getStage(currentStageId);
                
                record.moveToStage(t.getToStageId());
                Stage toStage = journey.getStage(t.getToStageId());
                
                for (StageTransitionObserver obs : observers) {
                    obs.onTransition(userId, journey.getJourneyId(), fromStage, toStage);
                }

                return;
            }
        }
    }

    private void expireIfNeeded(Journey journey) {
        if (journey.getState() != JourneyState.EXPIRED && journey.isExpired()) {
            journey.setState(JourneyState.EXPIRED);
            journeyRepo.save(journey);
        }
    }
}
