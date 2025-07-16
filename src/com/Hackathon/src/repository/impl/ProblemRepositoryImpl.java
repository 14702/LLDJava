package com.Hackathon.src.repository.impl;

import com.Hackathon.src.model.Problem;
import com.Hackathon.src.repository.interfaces.ProblemRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProblemRepositoryImpl implements ProblemRepository {

    private final Map<String, Problem> questionLibrary;

    public ProblemRepositoryImpl() {
        questionLibrary = new HashMap<>();
    }

    @Override
    public Problem addProblem(Problem problem) {
        System.out.println("Adding " + problem.getDescription() + " to repository");
        questionLibrary.put(problem.getId(), problem);
        return problem;
    }

    @Override
    public List<Problem> getAllProblems() {
        return new ArrayList<>(questionLibrary.values());
    }
}
