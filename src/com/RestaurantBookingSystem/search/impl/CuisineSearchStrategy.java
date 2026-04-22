package com.RestaurantBookingSystem.search.impl;

import com.RestaurantBookingSystem.enums.Cuisine;
import com.RestaurantBookingSystem.model.Restaurant;
import com.RestaurantBookingSystem.search.interfaces.SearchStrategy;

import java.util.List;
import java.util.stream.Collectors;

public class CuisineSearchStrategy implements SearchStrategy {
    @Override
    public List<Restaurant> search(List<Restaurant> restaurants, Object criteria) {
        Cuisine cuisine = (Cuisine) criteria;
        return restaurants.stream()
                .filter(r -> r.getCuisine() == cuisine)
                .collect(Collectors.toList());
    }
}
