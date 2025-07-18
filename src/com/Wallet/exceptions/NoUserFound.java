package com.Wallet.exceptions;

public class NoUserFound extends RuntimeException{
    public NoUserFound(String errMsg){
        super(errMsg);
    }
}