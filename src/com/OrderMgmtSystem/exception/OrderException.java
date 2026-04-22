package com.OrderMgmtSystem.exception;

public class OrderException extends RuntimeException {
    private final ErrorCode errorCode;

    public OrderException(ErrorCode errorCode, String message) {
        super(errorCode + ": " + message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() { return errorCode; }
}