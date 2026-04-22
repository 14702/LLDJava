package com.RestaurantBookingSystem.search.impl;

import com.RestaurantBookingSystem.model.Restaurant;
import com.RestaurantBookingSystem.search.interfaces.SearchStrategy;

import java.util.List;
import java.util.stream.Collectors;

public class CostForTwoSearchStrategy implements SearchStrategy {
    @Override
    public List<Restaurant> search(List<Restaurant> restaurants, Object criteria) {
        double maxCost = (Double) criteria;
        return restaurants.stream()
                .filter(r -> r.getCostForTwo() <= maxCost)
                .collect(Collectors.toList());
    }
}
