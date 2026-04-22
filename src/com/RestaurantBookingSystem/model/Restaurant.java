package com.RestaurantBookingSystem.model;

import com.RestaurantBookingSystem.enums.Cuisine;
import com.RestaurantBookingSystem.enums.FoodType;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Restaurant {
    private final String id;
    private final String name;
    private final String city;
    private final String area;
    private final Cuisine cuisine;
    private final FoodType foodType;
    private final double costForTwo;
    private final List<Table> tables;
    private final List<LocalTime> availableSlotStartTimes;
    private final ReentrantLock bookingLock = new ReentrantLock();

    public Restaurant(String id, String name, String city, String area, Cuisine cuisine,
                      FoodType foodType, double costForTwo, List<Table> tables,
                      List<LocalTime> availableSlotStartTimes) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.area = area;
        this.cuisine = cuisine;
        this.foodType = foodType;
        this.costForTwo = costForTwo;
        this.tables = new CopyOnWriteArrayList<>(tables);
        this.availableSlotStartTimes = new CopyOnWriteArrayList<>(availableSlotStartTimes);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCity() { return city; }
    public String getArea() { return area; }
    public Cuisine getCuisine() { return cuisine; }
    public FoodType getFoodType() { return foodType; }
    public double getCostForTwo() { return costForTwo; }
    public List<Table> getTables() { return tables; }
    public List<LocalTime> getAvailableSlotStartTimes() { return availableSlotStartTimes; }
    public ReentrantLock getBookingLock() { return bookingLock; }

    public void updateSlotStartTimes(List<LocalTime> newSlots) {
        availableSlotStartTimes.clear();
        availableSlotStartTimes.addAll(newSlots);
    }

    @Override
    public String toString() {
        return "Restaurant{name=" + name + ", city=" + city + ", area=" + area +
                ", cuisine=" + cuisine + ", foodType=" + foodType +
                ", costForTwo=" + costForTwo + ", tables=" + tables.size() + "}";
    }
}
