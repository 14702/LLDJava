package com.Hackathon.src.repository.impl;

import com.Hackathon.src.exceptions.ProblemNotFoundException;
import com.Hackathon.src.model.Problem;
import com.Hackathon.src.repository.interfaces.ProblemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ProblemRepositoryImpl implements ProblemRepository {
    private final ConcurrentHashMap<String, Problem> store = new ConcurrentHashMap<>();

    @Override
    public void addProblem(Problem problem) {
        store.put(problem.getId(), problem);
    }

    @Override
    public Problem getById(String problemId) {
        Problem p = store.get(problemId);
        if (p == null) throw new ProblemNotFoundException("Problem not found: " + problemId);
        return p;
    }

    @Override
    public List<Problem> getAll() {
        return new ArrayList<>(store.values());
    }
}