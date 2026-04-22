package com.MiniWallet.exception;

public enum ErrorCode {
    USER_NOT_FOUND("User not found"),
    USER_ALREADY_EXISTS("User already registered"),
    INSUFFICIENT_BALANCE("Insufficient wallet balance"),
    INVALID_AMOUNT("Transaction amount must be greater than 0"),
    SELF_TRANSFER("Cannot send money to yourself");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
