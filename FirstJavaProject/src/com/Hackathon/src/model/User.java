package com.Hackathon.src.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String name;
    private String email;
    private String departmentName;
    private List<Problem> solvedProblems;
    private int currentScore;

    public User(String id, String name, String departmentName, String email) {
        this.id = id;
        this.name = name;
        this.departmentName = departmentName;
        this.email = email;
        solvedProblems = new ArrayList<>();
        currentScore = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Problem> getSolvedProblems() {
        return solvedProblems;
    }

    public void setSolvedProblems(List<Problem> solvedProblems) {
        this.solvedProblems = solvedProblems;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }
}
