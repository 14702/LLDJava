package com.RestaurantBookingSystem.search.impl;

import com.RestaurantBookingSystem.model.Restaurant;
import com.RestaurantBookingSystem.search.interfaces.SearchStrategy;

import java.util.List;
import java.util.stream.Collectors;

public class NameSearchStrategy implements SearchStrategy {
    @Override
    public List<Restaurant> search(List<Restaurant> restaurants, Object criteria) {
        String name = (String) criteria;
        return restaurants.stream()
                .filter(r -> r.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
}
