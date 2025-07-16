package com.ParkingLot;
import java.util.ArrayList;
import java.util.List;

public class ParkingSpotManager {
    List<ParkingSpot> parkingSpots = new ArrayList<>();

    public void intializeSpots(int total){
        for(int i = 0 ; i  < total ; i++){
            parkingSpots.add(new ParkingSpot(i, 100, true, null));
        }
    }

    public void addSpot(ParkingSpot spot){
        parkingSpots.add(spot);
    }

    public void removeSpot(ParkingSpot spot){
        parkingSpots.remove(spot);
    }
}
