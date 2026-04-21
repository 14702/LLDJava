package com.Hackathon.src;

import com.Hackathon.src.enums.Difficulty;
import com.Hackathon.src.model.Problem;
import com.Hackathon.src.model.User;
import com.Hackathon.src.repository.impl.ProblemRepositoryImpl;
import com.Hackathon.src.repository.impl.UserRepositoryImpl;
import com.Hackathon.src.repository.interfaces.ProblemRepository;
import com.Hackathon.src.repository.interfaces.UserRepository;
import com.Hackathon.src.service.impl.ProblemServiceImpl;
import com.Hackathon.src.service.impl.UserServiceImpl;
import com.Hackathon.src.service.interfaces.ProblemService;
import com.Hackathon.src.service.interfaces.UserService;
import com.Hackathon.src.strategy.impl.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        // --- Wiring (DI by hand) ---
        ProblemRepository problemRepo = new ProblemRepositoryImpl();
        UserRepository userRepo = new UserRepositoryImpl();

        ProblemService problemService = new ProblemServiceImpl(problemRepo);
        UserService userService = new UserServiceImpl(
                userRepo, problemRepo,
                new StandardScoringStrategy(),
                new TagBasedRecommendationStrategy()
        );

        // ========== 1. Register Users ==========
        System.out.println("===== Register Users =====");
        userService.addUser("Somesh", "Engineering", "somesh@test.com");
        userService.addUser("Ashish", "Marketing", "ashish@test.com");
        userService.addUser("Priya", "Engineering", "priya@test.com");

        // ========== 2. Add Problems ==========
        System.out.println("\n===== Add Problems =====");
        Problem p1 = problemService.addProblem("Two Sum", "Arrays", Difficulty.EASY, 10);
        Problem p2 = problemService.addProblem("Merge Intervals", "Arrays", Difficulty.MEDIUM, 20);
        Problem p3 = problemService.addProblem("LRU Cache", "Design", Difficulty.HARD, 30);
        Problem p4 = problemService.addProblem("Valid Parentheses", "Stacks", Difficulty.EASY, 10);
        Problem p5 = problemService.addProblem("Median of Sorted Arrays", "Arrays", Difficulty.HARD, 30);
        Problem p6 = problemService.addProblem("Min Stack", "Stacks", Difficulty.MEDIUM, 20);

        // ========== 3. Fetch Problems (filter + sort) ==========
        System.out.println("\n===== Filter by Difficulty=EASY =====");
        List<Problem> easy = problemService.fetchProblems(
                new DifficultyFilterStrategyImpl(Difficulty.EASY), new ScoreSortStrategyImpl());
        easy.forEach(System.out::println);

        System.out.println("\n===== Filter by Tag=Arrays, Sort by Score =====");
        List<Problem> arrays = problemService.fetchProblems(
                new TagFilterStrategyImpl("Arrays"), new ScoreSortStrategyImpl());
        arrays.forEach(System.out::println);

        System.out.println("\n===== All problems sorted by Difficulty =====");
        List<Problem> byDiff = problemService.fetchProblems(null, new DifficultySortStrategyImpl());
        byDiff.forEach(System.out::println);

        // ========== 4. Solve Problems (with time tracking) ==========
        System.out.println("\n===== Solve Problems =====");
        List<Problem> recs;
        recs = userService.solve("somesh@test.com", p1.getId(), 120_000);
        System.out.println("  Recommendations: " + formatRecs(recs));

        recs = userService.solve("somesh@test.com", p2.getId(), 300_000);
        System.out.println("  Recommendations: " + formatRecs(recs));

        recs = userService.solve("somesh@test.com", p3.getId(), 600_000);
        System.out.println("  Recommendations: " + formatRecs(recs));

        recs = userService.solve("ashish@test.com", p1.getId(), 180_000);
        System.out.println("  Recommendations: " + formatRecs(recs));

        recs = userService.solve("ashish@test.com", p5.getId(), 450_000);
        System.out.println("  Recommendations: " + formatRecs(recs));

        recs = userService.solve("priya@test.com", p1.getId(), 90_000);
        System.out.println("  Recommendations: " + formatRecs(recs));

        recs = userService.solve("priya@test.com", p4.getId(), 60_000);
        System.out.println("  Recommendations: " + formatRecs(recs));

        // duplicate solve attempt
        userService.solve("somesh@test.com", p1.getId(), 100_000);

        // ========== 5. Fetch Solved Problems ==========
        System.out.println("\n===== Somesh's Solved Problems =====");
        userService.fetchSolvedProblems("somesh@test.com").forEach(System.out::println);

        // ========== 6. fetchProblems after solves (shows solve count + avg time) ==========
        System.out.println("\n===== All Problems (post-solve, sorted by score) =====");
        problemService.fetchProblems(null, new ScoreSortStrategyImpl()).forEach(System.out::println);

        // ========== 7. Problem Stats ==========
        System.out.println("\n===== Problem Stats =====");
        problemService.getUserCountForProblem(p1.getId());
        problemService.getAvgSolveTime(p1.getId());
        problemService.getUserCountForProblem(p3.getId());
        problemService.getAvgSolveTime(p3.getId());

        // ========== 8. getLeader() ==========
        System.out.println("\n===== Current Leader =====");
        User leader = userService.getLeader();
        System.out.println("Leader: " + leader.getName() + " [" + leader.getDepartment() + "]");

        // ========== 9. Individual Leaderboard ==========
        System.out.println("\n===== Leaderboard (Top 3) =====");
        List<User> leaders = userService.getLeaderboard(3);
        for (int i = 0; i < leaders.size(); i++) {
            User u = leaders.get(i);
            System.out.printf("#%d %s [%s] - %d pts%n", i + 1, u.getName(), u.getDepartment(), u.getCurrentScore());
        }

        // ========== 8. Department Leaderboard ==========
        System.out.println("\n===== Department Leaderboard =====");
        userService.getDepartmentLeaderboard(5).forEach(System.out::println);

        // ========== 9. Top N Liked Problems by Tag ==========
        System.out.println("\n===== Top 3 Liked Problems for Tag=Arrays =====");
        problemService.getTopNProblems(3, "Arrays").forEach(System.out::println);

        // ========== 10. Time-Weighted Scoring Demo ==========
        System.out.println("\n===== Time-Weighted Scoring Demo =====");
        UserService timedService = new UserServiceImpl(
                userRepo, problemRepo,
                new TimeWeightedScoringStrategy(120_000, 1.5),
                new PopularityRecommendationStrategy()
        );
        timedService.addUser("Rahul", "Engineering", "rahul@test.com");
        timedService.solve("rahul@test.com", p4.getId(), 60_000);   // fast -> bonus
        timedService.solve("rahul@test.com", p6.getId(), 500_000);  // slow -> normal

        System.out.println("\n===== Updated Leaderboard (Top 5) =====");
        userService.getLeaderboard(5).forEach(u ->
                System.out.printf("  %s [%s] - %d pts%n", u.getName(), u.getDepartment(), u.getCurrentScore()));
    }

    private static String formatRecs(List<Problem> recs) {
        if (recs.isEmpty()) return "none";
        StringBuilder sb = new StringBuilder();
        for (Problem p : recs) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(p.getDescription()).append("(").append(p.getDifficulty()).append(")");
        }
        return sb.toString();
    }
}
