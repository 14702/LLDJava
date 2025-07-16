package com.ParkingLot;

public class EntryGate {
    // for the entry vehicle -> find nearest spot using strategy -> assign vehicle to spot -> generate ticket

    // needs access to list of parking spots via manager (NEED TO PASS IT)
    ParkingStrategy parkingStrategy;
    ParkingSpotManager parkingSpotManager;

    EntryGate(ParkingStrategy parkingStrategy, ParkingSpotManager parkingSpotManager){
        this.parkingStrategy = parkingStrategy;
        this.parkingSpotManager = parkingSpotManager;
    }

    public ParkingSpot entryVehicle(Vehicle vehicle){
        ParkingSpot spot = parkingStrategy.findSpot(parkingSpotManager);
        spot.bookSpot(vehicle);
        return spot;
    }
}
