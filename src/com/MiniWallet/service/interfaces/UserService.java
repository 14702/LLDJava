package com.MiniWallet.service.interfaces;

import com.MiniWallet.model.User;

public interface UserService {
    User registerUser(String userId, String name, String email);
    User getUser(String userId);
}
