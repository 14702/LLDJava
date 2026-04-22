package com.ParkingLot.model;

public class ParkingSpot {
    private final int id;
    private final int chargePerHour;
    private volatile boolean available;
    private Vehicle vehicle;

    public ParkingSpot(int id, int chargePerHour) {
        this.id = id;
        this.chargePerHour = chargePerHour;
        this.available = true;
    }

    public synchronized void book(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.available = false;
    }

    public synchronized void free() {
        this.vehicle = null;
        this.available = true;
    }

    public int getId() { return id; }
    public int getChargePerHour() { return chargePerHour; }
    public boolean isAvailable() { return available; }
    public Vehicle getVehicle() { return vehicle; }
}
