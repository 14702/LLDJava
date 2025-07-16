package com.Hackathon.src.model;

import com.Hackathon.src.enums.Difficulty;
import com.Hackathon.src.enums.Status;

public class Problem {
    private String id;
    private String description;
    private String tag;
    private Difficulty problemDifficulty;
    private int score;
    private Status problemStatus;
    private int solvedCount;
    private int likes;

    public Problem(String id, String description, String tag, Difficulty problemDifficulty, int score) {
        this.id = id;
        this.description = description;
        this.tag = tag;
        this.problemDifficulty = problemDifficulty;
        this.score = score;
        problemStatus = Status.UNSOLVED;
        solvedCount = 0;
        likes = 0;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Difficulty getProblemDifficulty() {
        return problemDifficulty;
    }

    public void setProblemDifficulty(Difficulty problemDifficulty) {
        this.problemDifficulty = problemDifficulty;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Status getProblemStatus() {
        return problemStatus;
    }

    public void setProblemStatus(Status problemStatus) {
        this.problemStatus = problemStatus;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", tag='" + tag + '\'' +
                ", problemDifficulty=" + problemDifficulty +
                ", score=" + score +
                ", problemStatus=" + problemStatus +
                '}';
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getSolvedCount() {
        return solvedCount;
    }

    public void setSolvedCount(int solvedCount) {
        this.solvedCount = solvedCount;
    }
}
