package com.RestaurantBookingSystem.model;

import com.RestaurantBookingSystem.enums.BookingStatus;

public class Booking {
    private final String bookingId;
    private final String userId;
    private final String restaurantId;
    private final Table table;
    private final TimeSlot timeSlot;
    private final int numberOfPeople;
    private BookingStatus status;

    public Booking(String bookingId, String userId, String restaurantId,
                   Table table, TimeSlot timeSlot, int numberOfPeople) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.table = table;
        this.timeSlot = timeSlot;
        this.numberOfPeople = numberOfPeople;
        this.status = BookingStatus.CONFIRMED;
    }

    public String getBookingId() { return bookingId; }
    public String getUserId() { return userId; }
    public String getRestaurantId() { return restaurantId; }
    public Table getTable() { return table; }
    public TimeSlot getTimeSlot() { return timeSlot; }
    public int getNumberOfPeople() { return numberOfPeople; }
    public BookingStatus getStatus() { return status; }

    public void cancel() {
        this.status = BookingStatus.CANCELLED;
        table.releaseSlot(timeSlot);
    }

    @Override
    public String toString() {
        return "Booking{id=" + bookingId + ", restaurant=" + restaurantId +
                ", table=" + table.getTableId() + ", slot=" + timeSlot +
                ", people=" + numberOfPeople + ", status=" + status + "}";
    }
}
