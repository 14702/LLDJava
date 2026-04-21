package com.Hackathon.src.model;

public class SolveRecord {
    private final String problemId;
    private final long timeTakenMs;

    public SolveRecord(String problemId, long timeTakenMs) {
        this.problemId = problemId;
        this.timeTakenMs = timeTakenMs;
    }

    public String getProblemId() { return problemId; }
    public long getTimeTakenMs() { return timeTakenMs; }
}
