package com.RestaurantBookingSystem.service.impl;

import com.RestaurantBookingSystem.model.Restaurant;
import com.RestaurantBookingSystem.search.interfaces.SearchStrategy;
import com.RestaurantBookingSystem.service.interfaces.RestaurantService;
import com.RestaurantBookingSystem.service.interfaces.SearchService;

import java.util.List;

public class SearchServiceImpl implements SearchService {
    private final RestaurantService restaurantService;

    public SearchServiceImpl(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @Override
    public List<Restaurant> search(SearchStrategy strategy, Object criteria) {
        return strategy.search(restaurantService.getAllRestaurants(), criteria);
    }
}
