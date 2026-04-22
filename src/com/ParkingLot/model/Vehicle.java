package com.ParkingLot.model;

import com.ParkingLot.enums.VehicleType;

public class Vehicle {
    private final int num;
    private final VehicleType type;

    public Vehicle(int num, VehicleType type) {
        this.num = num;
        this.type = type;
    }

    public int getNum() { return num; }
    public VehicleType getType() { return type; }
}
