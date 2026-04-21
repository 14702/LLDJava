package com.Hackathon.src.service.impl;

import com.Hackathon.src.model.Problem;
import com.Hackathon.src.model.SolveRecord;
import com.Hackathon.src.model.User;
import com.Hackathon.src.repository.interfaces.ProblemRepository;
import com.Hackathon.src.repository.interfaces.UserRepository;
import com.Hackathon.src.service.interfaces.UserService;
import com.Hackathon.src.strategy.interfaces.RecommendationStrategy;
import com.Hackathon.src.strategy.interfaces.ScoringStrategy;

import java.util.*;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final ScoringStrategy scoringStrategy;
    private final RecommendationStrategy recommendationStrategy;

    public UserServiceImpl(UserRepository userRepository, ProblemRepository problemRepository,
                           ScoringStrategy scoringStrategy, RecommendationStrategy recommendationStrategy) {
        this.userRepository = userRepository;
        this.problemRepository = problemRepository;
        this.scoringStrategy = scoringStrategy;
        this.recommendationStrategy = recommendationStrategy;
    }

    @Override
    public void addUser(String name, String department, String email) {
        User user = new User(name, department, email);
        userRepository.addUser(user);
        System.out.println("User registered: " + user.getId() + " - " + name + " [" + department + "]");
    }

    @Override
    public List<Problem> solve(String email, String problemId, long timeTakenMs) {
        User user = userRepository.getByEmail(email);
        Problem problem = problemRepository.getById(problemId);

        boolean alreadySolved = user.getSolveHistory().stream()
                .anyMatch(r -> r.getProblemId().equals(problemId));
        if (alreadySolved) {
            System.out.println(user.getName() + " already solved " + problem.getDescription());
            return Collections.emptyList();
        }

        int awarded = scoringStrategy.calculateScore(problem, timeTakenMs);
        problem.recordSolve(timeTakenMs);
        user.recordSolve(problemId, timeTakenMs, awarded);
        System.out.printf("%s solved %s in %.1fs -> +%d points (total: %d)%n",
                user.getName(), problem.getDescription(), timeTakenMs / 1000.0,
                awarded, user.getCurrentScore());

        Set<String> solvedIds = user.getSolveHistory().stream()
                .map(SolveRecord::getProblemId)
                .collect(Collectors.toSet());
        return recommendationStrategy.recommend(problem, problemRepository.getAll(), solvedIds, 5);
    }

    @Override
    public List<Problem> fetchSolvedProblems(String email) {
        User user = userRepository.getByEmail(email);
        return user.getSolveHistory().stream()
                .map(r -> problemRepository.getById(r.getProblemId()))
                .collect(Collectors.toList());
    }

    @Override
    public User getLeader() {
        return userRepository.getAll().stream()
                .max(Comparator.comparingInt(User::getCurrentScore))
                .orElseThrow(() -> new IllegalStateException("No users registered"));
    }

    @Override
    public List<User> getLeaderboard(int topN) {
        return userRepository.getAll().stream()
                .sorted(Comparator.comparingInt(User::getCurrentScore).reversed()
                        .thenComparing(User::getName))
                .limit(topN)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getDepartmentLeaderboard(int topN) {
        Map<String, Integer> deptScores = new LinkedHashMap<>();
        for (User u : userRepository.getAll()) {
            deptScores.merge(u.getDepartment(), u.getCurrentScore(), Integer::sum);
        }
        return deptScores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry::getKey))
                .limit(topN)
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.toList());
    }
}
