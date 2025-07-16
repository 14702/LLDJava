package com.Hackathon.src.repository.interfaces;

import com.Hackathon.src.model.Problem;

import java.util.List;

public interface ProblemRepository {
    Problem addProblem(Problem problem);
    List<Problem> getAllProblems();
}
