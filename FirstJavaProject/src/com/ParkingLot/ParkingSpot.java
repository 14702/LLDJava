package com.ParkingLot;

public class ParkingSpot {
    int id;
    int charge;
    boolean isAvailable;
    Vehicle vehicle;

    ParkingSpot(int id, int charge, boolean isAvailable, Vehicle vehicle){
        this.id = id;
        this.charge = charge;
        this.isAvailable = isAvailable;
        this.vehicle = vehicle;
    }

    public void bookSpot(Vehicle vehicle){
        this.vehicle = vehicle;
        isAvailable = false;
    }

    public void freeSpot(){
        this.vehicle = null;
        isAvailable = true;
    }

}
