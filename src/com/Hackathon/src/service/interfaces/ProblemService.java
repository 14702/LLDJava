package com.Hackathon.src.service.interfaces;

import com.Hackathon.src.enums.Difficulty;
import com.Hackathon.src.model.Problem;
import com.Hackathon.src.strategy.interfaces.ProblemFilterStrategy;
import com.Hackathon.src.strategy.interfaces.ProblemSortStrategy;

public interface ProblemService {
    Problem addProblem(String description, String tagName, Difficulty problemDifficulty, int score);

    void viewSortedProblems(ProblemSortStrategy problemSortStrategy);

    void viewFilteredProblems(ProblemFilterStrategy filterStrategy);
    void getUserCountForProblem(Problem problem);

    void getMostLikedProblemByTag(ProblemFilterStrategy filterStrategy);
}
