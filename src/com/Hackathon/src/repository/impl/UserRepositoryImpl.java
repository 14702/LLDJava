package com.Hackathon.src.repository.impl;

import com.Hackathon.src.exceptions.DuplicateEntityException;
import com.Hackathon.src.exceptions.UserNotFoundException;
import com.Hackathon.src.model.User;
import com.Hackathon.src.repository.interfaces.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepositoryImpl implements UserRepository {
    private final ConcurrentHashMap<String, User> store = new ConcurrentHashMap<>();

    @Override
    public void addUser(User user) {
        if (store.putIfAbsent(user.getEmail(), user) != null) {
            throw new DuplicateEntityException("User already registered: " + user.getEmail());
        }
    }

    @Override
    public User getByEmail(String email) {
        User u = store.get(email);
        if (u == null) throw new UserNotFoundException("User not found: " + email);
        return u;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(store.values());
    }
}