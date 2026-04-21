package com.Hackathon.src.model;

import com.Hackathon.src.enums.Difficulty;
import com.Hackathon.src.enums.Status;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Problem {
    private static final AtomicInteger ID_GEN = new AtomicInteger(1);

    private final String id;
    private final String description;
    private final String tag;
    private final Difficulty difficulty;
    private final int score;
    private volatile Status status;
    private final AtomicInteger solvedCount;
    private final AtomicLong totalSolveTimeMs;
    private final AtomicInteger likes;

    public Problem(String description, String tag, Difficulty difficulty, int score) {
        if (description == null || description.isBlank()) throw new IllegalArgumentException("Description required");
        if (tag == null || tag.isBlank()) throw new IllegalArgumentException("Tag required");
        if (difficulty == null) throw new IllegalArgumentException("Difficulty required");
        if (score <= 0) throw new IllegalArgumentException("Score must be positive");

        this.id = "P" + ID_GEN.getAndIncrement();
        this.description = description;
        this.tag = tag;
        this.difficulty = difficulty;
        this.score = score;
        this.status = Status.UNSOLVED;
        this.solvedCount = new AtomicInteger(0);
        this.totalSolveTimeMs = new AtomicLong(0);
        this.likes = new AtomicInteger(score);
    }

    public void recordSolve(long timeTakenMs) {
        solvedCount.incrementAndGet();
        totalSolveTimeMs.addAndGet(timeTakenMs);
        status = Status.SOLVED;
    }

    public double getAvgSolveTimeSec() {
        int count = solvedCount.get();
        return count == 0 ? 0 : (totalSolveTimeMs.get() / 1000.0) / count;
    }

    public String getId() { return id; }
    public String getDescription() { return description; }
    public String getTag() { return tag; }
    public Difficulty getDifficulty() { return difficulty; }
    public int getScore() { return score; }
    public Status getStatus() { return status; }
    public int getSolvedCount() { return solvedCount.get(); }
    public int getLikes() { return likes.get(); }
    public void addLike() { likes.incrementAndGet(); }

    @Override
    public String toString() {
        return String.format("[%s] %s | Tag=%s | %s | Score=%d | Solved=%d | AvgTime=%.1fs",
                id, description, tag, difficulty, score, getSolvedCount(), getAvgSolveTimeSec());
    }
}