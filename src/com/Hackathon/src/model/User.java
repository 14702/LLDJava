package com.Hackathon.src.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class User {
    private static final AtomicInteger ID_GEN = new AtomicInteger(1);

    private final String id;
    private final String name;
    private final String department;
    private final String email;
    private final List<SolveRecord> solveHistory;
    private final AtomicInteger currentScore;

    public User(String name, String department, String email) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name required");
        if (department == null || department.isBlank()) throw new IllegalArgumentException("Department required");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email required");

        this.id = "U" + ID_GEN.getAndIncrement();
        this.name = name;
        this.department = department;
        this.email = email;
        this.solveHistory = new CopyOnWriteArrayList<>();
        this.currentScore = new AtomicInteger(0);
    }

    public void recordSolve(String problemId, long timeTakenMs, int score) {
        solveHistory.add(new SolveRecord(problemId, timeTakenMs));
        currentScore.addAndGet(score);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public String getEmail() { return email; }
    public List<SolveRecord> getSolveHistory() { return solveHistory; }
    public int getCurrentScore() { return currentScore.get(); }
}
