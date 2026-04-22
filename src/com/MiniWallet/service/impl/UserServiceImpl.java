package com.MiniWallet.service.impl;

import com.MiniWallet.exception.ErrorCode;
import com.MiniWallet.exception.WalletException;
import com.MiniWallet.model.User;
import com.MiniWallet.model.Wallet;
import com.MiniWallet.service.interfaces.UserService;

import java.util.concurrent.ConcurrentHashMap;

public class UserServiceImpl implements UserService {
    private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Wallet> wallets = new ConcurrentHashMap<>();

    @Override
    public User registerUser(String userId, String name, String email) {
        User user = new User(userId, name, email);
        if (users.putIfAbsent(userId, user) != null) {
            throw new WalletException(ErrorCode.USER_ALREADY_EXISTS);
        }
        wallets.put(userId, new Wallet(userId));
        return user;
    }

    @Override
    public User getUser(String userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new WalletException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    public Wallet getWallet(String userId) {
        getUser(userId);
        return wallets.get(userId);
    }
}
