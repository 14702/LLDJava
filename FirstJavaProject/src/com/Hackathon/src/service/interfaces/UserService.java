package com.Hackathon.src.service.interfaces;

import com.Hackathon.src.model.Problem;
import com.Hackathon.src.model.User;

public interface UserService {
    void addUser(String name, String departmentName, String email);

    void solveProblem(String s, Problem problem);

    void viewUserSolvedProblems(String id);

    User getCurrentLeaderOfContest();
}
