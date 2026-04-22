package com.RestaurantBookingSystem.exception;

public enum ErrorCode {
    RESTAURANT_NOT_FOUND("Restaurant not found"),
    SLOT_NOT_AVAILABLE("Requested slot is not available"),
    BOOKING_DATE_OUT_OF_RANGE("Booking date exceeds the allowed future window"),
    INVALID_PEOPLE_COUNT("Number of people must be positive"),
    DUPLICATE_RESTAURANT("Restaurant with this name already exists in the same area"),
    BOOKING_NOT_FOUND("Booking not found"),
    INVALID_SLOT("Invalid time slot"),
    NO_TABLES_AVAILABLE("No tables available for the requested slot");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
