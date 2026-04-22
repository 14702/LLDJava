package com.Wallet.repository;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import com.Wallet.exceptions.NoUserFound;
import com.Wallet.model.User;

public class UserData {
    Map<String, User> userIdToUser = new ConcurrentHashMap<>();

    public User getUserById(String id){
        User user = userIdToUser.get(id);
        if(Objects.isNull(id)) throw new NoUserFound("No user found with Id: " + id);
        return user;
    }

    public Map<String, User> getUserIdToUser(){
        return this.userIdToUser;
    }
}