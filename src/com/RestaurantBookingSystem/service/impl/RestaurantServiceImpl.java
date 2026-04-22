package com.RestaurantBookingSystem.service.impl;

import com.RestaurantBookingSystem.exception.BookingException;
import com.RestaurantBookingSystem.exception.ErrorCode;
import com.RestaurantBookingSystem.model.Restaurant;
import com.RestaurantBookingSystem.service.interfaces.RestaurantService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RestaurantServiceImpl implements RestaurantService {
    private final ConcurrentHashMap<String, Restaurant> restaurants = new ConcurrentHashMap<>();

    @Override
    public Restaurant registerRestaurant(Restaurant restaurant) {
        if (restaurants.putIfAbsent(restaurant.getId(), restaurant) != null) {
            throw new BookingException(ErrorCode.DUPLICATE_RESTAURANT);
        }
        return restaurant;
    }

    @Override
    public void updateTimeSlots(String restaurantId, List<LocalTime> newSlots) {
        Restaurant restaurant = getRestaurant(restaurantId);
        restaurant.updateSlotStartTimes(newSlots);
    }

    @Override
    public Restaurant getRestaurant(String restaurantId) {
        Restaurant restaurant = restaurants.get(restaurantId);
        if (restaurant == null) {
            throw new BookingException(ErrorCode.RESTAURANT_NOT_FOUND);
        }
        return restaurant;
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return new ArrayList<>(restaurants.values());
    }
}
