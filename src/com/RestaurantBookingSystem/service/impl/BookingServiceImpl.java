package com.RestaurantBookingSystem.service.impl;

import com.RestaurantBookingSystem.exception.BookingException;
import com.RestaurantBookingSystem.exception.ErrorCode;
import com.RestaurantBookingSystem.model.Booking;
import com.RestaurantBookingSystem.model.Restaurant;
import com.RestaurantBookingSystem.model.Table;
import com.RestaurantBookingSystem.model.TimeSlot;
import com.RestaurantBookingSystem.service.interfaces.BookingService;
import com.RestaurantBookingSystem.service.interfaces.RestaurantService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BookingServiceImpl implements BookingService {
    private final RestaurantService restaurantService;
    private final int maxFutureDays;
    private final ConcurrentHashMap<String, Booking> bookings = new ConcurrentHashMap<>();

    public BookingServiceImpl(RestaurantService restaurantService, int maxFutureDays) {
        this.restaurantService = restaurantService;
        this.maxFutureDays = maxFutureDays;
    }

    @Override
    public Booking bookTable(String userId, String restaurantId, int numberOfPeople,
                             LocalDate date, LocalTime slotStartTime) {
        if (numberOfPeople <= 0) {
            throw new BookingException(ErrorCode.INVALID_PEOPLE_COUNT);
        }

        LocalDate maxDate = LocalDate.now().plusDays(maxFutureDays);
        if (date.isAfter(maxDate) || date.isBefore(LocalDate.now())) {
            throw new BookingException(ErrorCode.BOOKING_DATE_OUT_OF_RANGE);
        }

        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);

        if (!restaurant.getAvailableSlotStartTimes().contains(slotStartTime)) {
            throw new BookingException(ErrorCode.INVALID_SLOT);
        }

        TimeSlot slot = new TimeSlot(date, slotStartTime);

        restaurant.getBookingLock().lock();
        try {
            for (Table table : restaurant.getTables()) {
                if (table.getCapacity() >= numberOfPeople && table.isAvailable(slot)) {
                    table.bookSlot(slot);
                    String bookingId = UUID.randomUUID().toString().substring(0, 8);
                    Booking booking = new Booking(bookingId, userId, restaurantId,
                            table, slot, numberOfPeople);
                    bookings.put(bookingId, booking);
                    return booking;
                }
            }
        } finally {
            restaurant.getBookingLock().unlock();
        }

        throw new BookingException(ErrorCode.NO_TABLES_AVAILABLE);
    }

    @Override
    public void cancelBooking(String bookingId) {
        Booking booking = getBooking(bookingId);
        Restaurant restaurant = restaurantService.getRestaurant(booking.getRestaurantId());
        restaurant.getBookingLock().lock();
        try {
            booking.cancel();
        } finally {
            restaurant.getBookingLock().unlock();
        }
    }

    @Override
    public Booking getBooking(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            throw new BookingException(ErrorCode.BOOKING_NOT_FOUND);
        }
        return booking;
    }
}
