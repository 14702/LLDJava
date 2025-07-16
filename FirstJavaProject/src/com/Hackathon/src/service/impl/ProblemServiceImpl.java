package com.Hackathon.src.service.impl;

import com.Hackathon.src.enums.Difficulty;
import com.Hackathon.src.exceptions.ProblemNotFoundException;
import com.Hackathon.src.model.Problem;
import com.Hackathon.src.repository.interfaces.ProblemRepository;
import com.Hackathon.src.repository.impl.ProblemRepositoryImpl;
import com.Hackathon.src.service.interfaces.ProblemService;
import com.Hackathon.src.strategy.interfaces.ProblemFilterStrategy;
import com.Hackathon.src.strategy.interfaces.ProblemSortStrategy;
import com.Hackathon.src.strategy.impl.TagFilterStrategyImpl;

import java.util.List;
import java.util.UUID;

import static com.Hackathon.src.constants.AppConstant.IS;
import static com.Hackathon.src.constants.AppConstant.MOST_LIKED_PROBLEM_FOR_A_TAG;
import static com.Hackathon.src.constants.AppConstant.PROBLEM_NOT_FOUND;
import static com.Hackathon.src.constants.AppConstant.SOLVED_COUNT;

public class ProblemServiceImpl implements ProblemService {

    private static ProblemServiceImpl instance;
    private final ProblemRepository problemRepository;

    public static synchronized ProblemServiceImpl getInstance() {
        if (instance == null) {
            instance = new ProblemServiceImpl();
        }
        return instance;
    }

    public ProblemServiceImpl() {
        problemRepository = new ProblemRepositoryImpl();
    }

    @Override
    public Problem addProblem(String description, String tagName, Difficulty problemDifficulty, int score) {
        Problem problem = createProblem(description, tagName, problemDifficulty, score);
        problem.setLikes(score);
        return problemRepository.addProblem(problem);
    }

    private Problem createProblem(String description, String tagName, Difficulty problemDifficulty, int score) {
        return new Problem(generateId(), description, tagName, problemDifficulty, score);
    }

    @Override
    public void viewSortedProblems(ProblemSortStrategy sortStrategy) {
        List<Problem> problems = getAllProblems();
        sortStrategy.sort(problems);
        System.out.println("--------------");
        System.out.println("Viewing Sorted Problems");
        printProblems(problems);
    }

    @Override
    public void viewFilteredProblems(ProblemFilterStrategy filterStrategy) {
        List<Problem> filteredProblems = filterStrategy.filter(getAllProblems());
        System.out.println("--------------");
        //System.out.println("Viewing Filtered Problems");
        printProblems(filteredProblems);
    }

    @Override
    public void getUserCountForProblem(Problem problem) {
        System.out.println(SOLVED_COUNT + problem.getDescription() + IS + problem.getSolvedCount());
    }

    @Override
    public void getMostLikedProblemByTag(ProblemFilterStrategy filterStrategy) {
        List<Problem> problems = filterStrategy.filter(getAllProblems());

        if (problems.isEmpty()) {
            throw new ProblemNotFoundException(PROBLEM_NOT_FOUND);
        }

        int maxLikes = getMaxLikes(problems);

        List<Problem> maxLikedProblems = problems.stream()
                .filter(p -> p.getLikes() == maxLikes)
                .toList();
        System.out.println("--------------");
        if (filterStrategy instanceof TagFilterStrategyImpl tagFilter) {
            System.out.println(MOST_LIKED_PROBLEM_FOR_A_TAG + tagFilter.getTag());
        }
        printProblems(maxLikedProblems);
    }

    private int getMaxLikes(List<Problem> problems) {
        return problems.stream()
                .mapToInt(Problem::getLikes)
                .max()
                .orElse(0);
    }


    private List<Problem> getAllProblems() {
        return problemRepository.getAllProblems();
    }

    public static void printProblems(List<Problem> problems) {
        for (Problem problem : problems) {
            //System.out.println(problem.toString());
            System.out.println("Problem description : " +problem.getDescription() + " , Tag : " + problem.getTag() + " , Difficulty : " + problem.getProblemDifficulty() + " , Score : "+ problem.getScore());
        }
        System.out.println("--------------");
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }
}
