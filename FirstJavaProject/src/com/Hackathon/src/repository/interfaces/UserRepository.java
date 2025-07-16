package com.Hackathon.src.repository.interfaces;

import com.Hackathon.src.model.User;

import java.util.List;

public interface UserRepository {
    void registerUser(User user);

    User getUserById(String id);

    List<User> getAllUsers();
}
