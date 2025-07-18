package com.Wallet.exceptions;

public class UserAlreadyExist extends RuntimeException{
    public UserAlreadyExist(String errMsg){
        super(errMsg);
    }
}