package com.Hackathon.src;

import com.Hackathon.src.enums.Difficulty;
import com.Hackathon.src.model.Problem;
import com.Hackathon.src.model.User;
import com.Hackathon.src.service.interfaces.ProblemService;
import com.Hackathon.src.service.interfaces.UserService;
import com.Hackathon.src.service.impl.ProblemServiceImpl;
import com.Hackathon.src.service.impl.UserServiceImpl;
import com.Hackathon.src.strategy.impl.DifficultyFilterStrategyImpl;
import com.Hackathon.src.strategy.impl.DifficultySortStrategyImpl;
import com.Hackathon.src.strategy.impl.ScoreSortStrategyImpl;
import com.Hackathon.src.strategy.impl.TagFilterStrategyImpl;

public class Hackathon {
    public static void main(String[] args) {
        UserService userService = UserServiceImpl.getInstance();
        ProblemService problemService = ProblemServiceImpl.getInstance();

        //adding User
        userService.addUser("Somesh", "Engineering", "somesh@hotmail.com");
        userService.addUser("Ashish", "Marketing", "ashish@hotmail.com");

        //adding Problem
        Problem problem1 = problemService.addProblem("Problem_1", "Tag1", Difficulty.MEDIUM, 20);
        Problem problem2 = problemService.addProblem("Problem_2", "Tag1", Difficulty.HARD, 30);
        Problem problem3 = problemService.addProblem("Problem_3", "Tag1", Difficulty.HARD, 20);
        Problem problem4 = problemService.addProblem("Problem_4", "Tag1", Difficulty.EASY, 10);

        // viewing problems sorted based on score and difficulty
        problemService.viewSortedProblems(new ScoreSortStrategyImpl());
        problemService.viewSortedProblems(new DifficultySortStrategyImpl());

        // viewing problems filtered based on difficulty and Tag
        problemService.viewFilteredProblems(new DifficultyFilterStrategyImpl(Difficulty.EASY));
        problemService.viewFilteredProblems(new TagFilterStrategyImpl("Tag1"));

        // Setting problems status to "SOLVED"
        userService.solveProblem("somesh@hotmail.com", problem1);
        userService.solveProblem("somesh@hotmail.com", problem2);
        userService.solveProblem("ashish@hotmail.com", problem3);
        userService.solveProblem("ashish@hotmail.com", problem4);

        userService.viewUserSolvedProblems("somesh@hotmail.com");
        problemService.getUserCountForProblem(problem1);

        User user = userService.getCurrentLeaderOfContest();
        System.out.println("Current Leader of Contest is : " + user.getName() + " with Score : " + user.getCurrentScore());

        problemService.getMostLikedProblemByTag(new TagFilterStrategyImpl("Tag1"));


    }
}

