package com.Hackathon.src.repository.interfaces;

import com.Hackathon.src.model.User;

import java.util.List;

public interface UserRepository {
    void addUser(User user);
    User getByEmail(String email);
    List<User> getAll();
}