package com.RestaurantBookingSystem.exception;

public class BookingException extends RuntimeException {
    private final ErrorCode errorCode;

    public BookingException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
