package com.RestaurantBookingSystem.service.interfaces;

import com.RestaurantBookingSystem.model.Restaurant;
import com.RestaurantBookingSystem.search.interfaces.SearchStrategy;

import java.util.List;

public interface SearchService {
    List<Restaurant> search(SearchStrategy strategy, Object criteria);
}
