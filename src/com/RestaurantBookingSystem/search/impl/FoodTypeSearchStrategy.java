package com.RestaurantBookingSystem.search.impl;

import com.RestaurantBookingSystem.enums.FoodType;
import com.RestaurantBookingSystem.model.Restaurant;
import com.RestaurantBookingSystem.search.interfaces.SearchStrategy;

import java.util.List;
import java.util.stream.Collectors;

public class FoodTypeSearchStrategy implements SearchStrategy {
    @Override
    public List<Restaurant> search(List<Restaurant> restaurants, Object criteria) {
        FoodType foodType = (FoodType) criteria;
        return restaurants.stream()
                .filter(r -> r.getFoodType() == foodType || r.getFoodType() == FoodType.BOTH)
                .collect(Collectors.toList());
    }
}
