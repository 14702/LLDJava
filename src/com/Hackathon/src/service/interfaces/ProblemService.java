package com.Hackathon.src.service.interfaces;

import com.Hackathon.src.enums.Difficulty;
import com.Hackathon.src.model.Problem;
import com.Hackathon.src.strategy.interfaces.ProblemFilterStrategy;
import com.Hackathon.src.strategy.interfaces.ProblemSortStrategy;

import java.util.List;

public interface ProblemService {
    Problem addProblem(String description, String tag, Difficulty difficulty, int score);
    List<Problem> fetchProblems(ProblemFilterStrategy filter, ProblemSortStrategy sort);
    void getUserCountForProblem(String problemId);
    void getAvgSolveTime(String problemId);
    List<Problem> getTopNProblems(int n, String tag);
}
