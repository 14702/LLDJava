package com.RestaurantBookingSystem.service.interfaces;

import com.RestaurantBookingSystem.model.Booking;

import java.time.LocalDate;
import java.time.LocalTime;

public interface BookingService {
    Booking bookTable(String userId, String restaurantId, int numberOfPeople,
                      LocalDate date, LocalTime slotStartTime);
    void cancelBooking(String bookingId);
    Booking getBooking(String bookingId);
}
