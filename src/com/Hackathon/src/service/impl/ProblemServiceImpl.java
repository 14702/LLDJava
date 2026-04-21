package com.Hackathon.src.service.impl;

import com.Hackathon.src.enums.Difficulty;
import com.Hackathon.src.model.Problem;
import com.Hackathon.src.repository.interfaces.ProblemRepository;
import com.Hackathon.src.service.interfaces.ProblemService;
import com.Hackathon.src.strategy.interfaces.ProblemFilterStrategy;
import com.Hackathon.src.strategy.interfaces.ProblemSortStrategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProblemServiceImpl implements ProblemService {
    private final ProblemRepository problemRepository;

    public ProblemServiceImpl(ProblemRepository problemRepository) {
        this.problemRepository = problemRepository;
    }

    @Override
    public Problem addProblem(String description, String tag, Difficulty difficulty, int score) {
        Problem problem = new Problem(description, tag, difficulty, score);
        problemRepository.addProblem(problem);
        System.out.println("Problem " + problem.getId() + " added: " + description);
        return problem;
    }

    @Override
    public List<Problem> fetchProblems(ProblemFilterStrategy filter, ProblemSortStrategy sort) {
        List<Problem> result = new ArrayList<>(problemRepository.getAll());
        if (filter != null) result = filter.filter(result);
        if (sort != null) sort.sort(result);
        return result;
    }

    @Override
    public void getUserCountForProblem(String problemId) {
        Problem p = problemRepository.getById(problemId);
        System.out.println("Users who solved " + p.getDescription() + ": " + p.getSolvedCount());
    }

    @Override
    public void getAvgSolveTime(String problemId) {
        Problem p = problemRepository.getById(problemId);
        System.out.printf("Avg solve time for %s: %.1fs%n", p.getDescription(), p.getAvgSolveTimeSec());
    }

    @Override
    public List<Problem> getTopNProblems(int n, String tag) {
        return problemRepository.getAll().stream()
                .filter(p -> p.getTag().equalsIgnoreCase(tag))
                .sorted(Comparator.comparingInt(Problem::getLikes).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }
}
