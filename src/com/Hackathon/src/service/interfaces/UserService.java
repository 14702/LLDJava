package com.Hackathon.src.service.interfaces;

import com.Hackathon.src.model.Problem;
import com.Hackathon.src.model.User;

import java.util.List;

public interface UserService {
    void addUser(String name, String department, String email);
    List<Problem> solve(String email, String problemId, long timeTakenMs);
    List<Problem> fetchSolvedProblems(String email);
    User getLeader();
    List<User> getLeaderboard(int topN);
    List<String> getDepartmentLeaderboard(int topN);
}
