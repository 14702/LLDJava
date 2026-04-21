package com.Hackathon.src.repository.interfaces;

import com.Hackathon.src.model.Problem;

import java.util.List;

public interface ProblemRepository {
    void addProblem(Problem problem);
    Problem getById(String problemId);
    List<Problem> getAll();
}