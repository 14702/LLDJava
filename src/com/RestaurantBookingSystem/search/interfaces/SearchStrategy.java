package com.RestaurantBookingSystem.search.interfaces;

import com.RestaurantBookingSystem.model.Restaurant;

import java.util.List;

public interface SearchStrategy {
    List<Restaurant> search(List<Restaurant> restaurants, Object criteria);
}
