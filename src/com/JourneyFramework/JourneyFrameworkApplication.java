package com.JourneyFramework;

import com.JourneyFramework.enums.*;
import com.JourneyFramework.exception.JourneyException;
import com.JourneyFramework.model.*;
import com.JourneyFramework.observer.impl.SmsNotificationObserver;
import com.JourneyFramework.repository.impl.JourneyRepositoryImpl;
import com.JourneyFramework.repository.impl.UserJourneyRepositoryImpl;
import com.JourneyFramework.service.impl.JourneyServiceImpl;
import com.JourneyFramework.service.interfaces.JourneyService;

import java.util.*;

public class JourneyFrameworkApplication { 

// Can also use state desing pattern, but not using as eahc states dont have much of a different behavior. 
// Also each state dont have much of complex logic, so not creating addional state classes and using simple transition table to manage state transitions. 

// USed observer pattern to notify about stage transitions (with  sms). we can add more obsrvers without changing service

    public static void main(String[] args) {
        JourneyService jrnySvc1 = new JourneyServiceImpl( new JourneyRepositoryImpl(), new UserJourneyRepositoryImpl());
        jrnySvc1.addObserver( new SmsNotificationObserver() );

    // create journey : StageA ->> StageB -> StageC 
        Journey j = new Journey("j1", "Recharge Journey", JourneyType.PERPETUAL, null, null);
        j.addStage( new Stage("a", "Stage A", StageType.ONBOARDING, true) );
        j.addStage(new Stage ("b", "Stage B", StageType.ONWARD, true) );
        j.addStage( new Stage("c", "Stage C", StageType.TERMINAL, false) );
        
        j.setOnboardingCondition( new Condition(Map.of("action", "register", "platform", "phonepe")) );
        j.addTransition( new Transition ("a", "b", new Condition(Map.of("action", "open_recharge")), "Open Recharge Page"));
        j.addTransition(new Transition("b", "c", new Condition(Map.of("action", "do_recharge")), "Do Recharge"));
        jrnySvc1.createJourney(j);
        System.out.println( "created journey : " + jrnySvc1.getJourney("j1").getName() );


    // only allowed transitions will work
        jrnySvc1.updateState("j1", JourneyState.ACTIVE);
        System.out.println( "state changed to : " + jrnySvc1.getJourney("j1").getState() );
        
        // non allowed transitions throws exception
        try {jrnySvc1.updateState("j1", JourneyState.CREATED);
        } catch (JourneyException e) {
            System.out.println("blocked invalid transition : " + e);
        }


    // check payload matches with onboarding condition & transitions
        jrnySvc1.evaluate("user1", new Payload(Map.of("action", "register", "platform", "phonepe")));
        System.out.println("user1 onboarded : " + jrnySvc1.isOnboarded("user1", "j1"));
        System.out.println("user1 current stage : " + jrnySvc1.getCurrentStage("user1", "j1").getName());


    // move user1 through opem recharge and do recharge stages
        jrnySvc1.evaluate("user1", new Payload(Map.of("action", "open_recharge")));
        System.out.println("user1 after open_recharge : " + jrnySvc1.getCurrentStage("user1", "j1").getName());

        jrnySvc1.evaluate("user1", new Payload(Map.of("action", "do_recharge")));
        System.out.println("user1 after do_recharge  : " + jrnySvc1.getCurrentStage("user1", "j1").getName());


    // ignore non matching payload 
        jrnySvc1.evaluate("user2", new Payload(Map.of("action", "random")));
        System.out.println("user2 onboarded after random payload : " + jrnySvc1.isOnboarded("user2", "j1"));


    // for timebound journey: endDate already passed, so getJourney will automatcially mark it as EXPIRED
        Date past = new Date(System.currentTimeMillis() - 3600_000);
        Journey timeBoundJrny = new Journey("j2", "Expired Journey", JourneyType.TIME_BOUND, new Date(System.currentTimeMillis() - 7200_000), past);
        timeBoundJrny.addStage(new Stage("x", "X", StageType.ONBOARDING));
        timeBoundJrny.addStage(new Stage("y", "Y", StageType.TERMINAL));
        timeBoundJrny.setOnboardingCondition(new Condition(Map.of("a", "b")));
        jrnySvc1.createJourney(timeBoundJrny);
        timeBoundJrny.setState(JourneyState.ACTIVE);
        System.out.println("time bound journey state : " + jrnySvc1.getJourney("j2").getState());


    // for recurring journey, same user can go through the journey multiple times
        JourneyService jrnySvc2 = new JourneyServiceImpl(new JourneyRepositoryImpl(), new UserJourneyRepositoryImpl());
        Journey recurrJrny = new Journey("jr", "Recurring", JourneyType.PERPETUAL, null, null, true);
        recurrJrny.addStage(new Stage("a", "Start", StageType.ONBOARDING));
        recurrJrny.addStage(new Stage("b", "End", StageType.TERMINAL));
        recurrJrny.setOnboardingCondition(new Condition(Map.of("action", "go")));
        recurrJrny.addTransition(new Transition("a", "b", new Condition(Map.of("action", "finish")), "Finish"));
        jrnySvc2.createJourney(recurrJrny);
        jrnySvc2.updateState("jr", JourneyState.ACTIVE);

        jrnySvc2.evaluate("u1", new Payload(Map.of("action", "go")));
        jrnySvc2.evaluate("u1", new Payload(Map.of("action", "finish")));
        System.out.println("recurring u1 at terminal : " + jrnySvc2.getCurrentStage("u1", "jr").getName());

        jrnySvc2.evaluate("u1", new Payload(Map.of("action", "go")));
        System.out.println("recurring u1 re onboarded : " + jrnySvc2.getCurrentStage("u1", "jr").getName());


    // error scnearios 
        System.out.println("Checking error case: " );
        try { 
            jrnySvc1.getJourney("nope");  
        } catch (JourneyException e) { 
            System.out.println("  first error Case: " + e); }
        
        try {
             jrnySvc1.getCurrentStage("ghost", "j1"); } 
        catch (JourneyException e) 
            { System.out.println("  second error Case: " + e); }
    }
}
