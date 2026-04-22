package com.RestaurantBookingSystem.service.interfaces;

import com.RestaurantBookingSystem.model.Restaurant;

import java.time.LocalTime;
import java.util.List;

public interface RestaurantService {
    Restaurant registerRestaurant(Restaurant restaurant);
    void updateTimeSlots(String restaurantId, List<LocalTime> newSlots);
    Restaurant getRestaurant(String restaurantId);
    List<Restaurant> getAllRestaurants();
}
