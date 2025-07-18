package com.Wallet.service.impl;

import java.util.Objects;
import com.Wallet.datastore.UserData;
import com.Wallet.exceptions.UserAlreadyExist;
import com.Wallet.model.User;
import com.Wallet.service.UserService;
import com.Wallet.service.WalletServiceInternal;

public class UserServiceImpl implements UserService {

    private UserData userData;

    private WalletServiceInternal walletService;

    public UserServiceImpl(UserData userData, WalletServiceInternal walletService){
        this.userData = userData;
        this.walletService = walletService;
    }

    @Override
    public User registerUser(String username) {
        System.out.println("Registering user " + username);
        User newUser = new User(username, username);
        User user = userData.getUserIdToUser().get(username);
        if(Objects.nonNull(user)){
            throw new UserAlreadyExist("User already exist");
        }
        userData.getUserIdToUser().put(username, newUser);
        walletService.createWallet(username);
        System.out.println("User " + username + " is registered");
        return newUser;
    }
}