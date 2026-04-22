package com.RestaurantBookingSystem.search.impl;

import com.RestaurantBookingSystem.model.Restaurant;
import com.RestaurantBookingSystem.search.interfaces.SearchStrategy;

import java.util.List;
import java.util.stream.Collectors;

public class CitySearchStrategy implements SearchStrategy {
    @Override
    public List<Restaurant> search(List<Restaurant> restaurants, Object criteria) {
        String city = (String) criteria;
        return restaurants.stream()
                .filter(r -> r.getCity().equalsIgnoreCase(city))
                .collect(Collectors.toList());
    }
}
