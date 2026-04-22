package com.RestaurantBookingSystem.search.impl;

import com.RestaurantBookingSystem.model.Restaurant;
import com.RestaurantBookingSystem.search.interfaces.SearchStrategy;

import java.util.List;
import java.util.stream.Collectors;

public class AreaSearchStrategy implements SearchStrategy {
    @Override
    public List<Restaurant> search(List<Restaurant> restaurants, Object criteria) {
        String area = (String) criteria;
        return restaurants.stream()
                .filter(r -> r.getArea().equalsIgnoreCase(area))
                .collect(Collectors.toList());
    }
}
