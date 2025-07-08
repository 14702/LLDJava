package com.ParkingLot;

public class Ticket {
    int id;
    int duration;
    Vehicle vehicle;
    ParkingSpot parkingSpot;

    Ticket(int id, int duration, Vehicle vehicle, ParkingSpot parkingSpot){
        this.id = id;
        this.duration = duration;
        this.vehicle =vehicle;
        this.parkingSpot = parkingSpot;
    }

    public static int generateTicket(){
        int id = (int)(Math.random() * (100));
        return id;
    }
}
