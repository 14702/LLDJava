package com.Hackathon.src.service.impl;

import com.Hackathon.src.enums.Status;
import com.Hackathon.src.model.Problem;
import com.Hackathon.src.model.User;
import com.Hackathon.src.repository.interfaces.UserRepository;
import com.Hackathon.src.repository.impl.UserRepositoryImpl;
import com.Hackathon.src.service.interfaces.UserService;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static com.Hackathon.src.constants.AppConstant.PROBLEMS_SOLVED_BY_USER;
import static com.Hackathon.src.constants.AppConstant.USER_NOT_FOUND;

public class UserServiceImpl implements UserService {

    private static UserService instance;
    private final UserRepository userRepository;

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserServiceImpl();
        }
        return  instance;
    }

    public UserServiceImpl() {
        userRepository = new UserRepositoryImpl();
    }

    @Override
    public void addUser(String name, String departmentName, String email) {
        User user = new User(generateId(), name, departmentName, email);
        userRepository.registerUser(user);

    }

    @Override
    public void solveProblem(String id, Problem problem) {
        User user = getUser(id);
        if (user == null) {
            throw new IllegalArgumentException(USER_NOT_FOUND + id);
        }
        problem.setSolvedCount(problem.getSolvedCount() + 1); 
        problem.setProblemStatus(Status.SOLVED);
        user.getSolvedProblems().add(problem);
        user.setCurrentScore(user.getCurrentScore() + problem.getScore());
//        getRecommendation(problem.getTag());
    }


    private User getUser(String id) {
        return userRepository.getUserById(id);
    }

    @Override
    public void viewUserSolvedProblems(String id) {
        User user = getUser(id);
        if (user == null) {
            throw new IllegalArgumentException(USER_NOT_FOUND + id);
        }
        System.out.println(PROBLEMS_SOLVED_BY_USER + user.getName() + ":");
        ProblemServiceImpl.printProblems(user.getSolvedProblems());
    }

    @Override
    public User getCurrentLeaderOfContest() {
        return getAllUsers().stream()
                .max(Comparator.comparingInt(User::getCurrentScore))
                .orElse(null);
    }

    private List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }
}
