package com.ParkingLot.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Ticket {
    private static final AtomicInteger ID_GEN = new AtomicInteger(0);

    private final int id;
    private final int duration;
    private final Vehicle vehicle;
    private final ParkingSpot spot;

    public Ticket(int duration, Vehicle vehicle, ParkingSpot spot) {
        this.id = ID_GEN.incrementAndGet();
        this.duration = duration;
        this.vehicle = vehicle;
        this.spot = spot;
    }

    public int getId() { return id; }
    public int getDuration() { return duration; }
    public Vehicle getVehicle() { return vehicle; }
    public ParkingSpot getSpot() { return spot; }
    public int getCost() { return duration * spot.getChargePerHour(); }
}
