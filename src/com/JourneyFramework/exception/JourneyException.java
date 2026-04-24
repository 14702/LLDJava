package com.JourneyFramework.exception;

import com.JourneyFramework.enums.ErrorCode;

public class JourneyException extends RuntimeException {
    private final ErrorCode errorCode;

    public JourneyException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return "[" + errorCode + "] " + getMessage();
    }
}
