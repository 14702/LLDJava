package com.ParkingLot;
import java.util.List;
public class NearestParkingStrategy implements ParkingStrategy{
    int spot;
    @Override
    public ParkingSpot findSpot(ParkingSpotManager parkingSpotManager){
        // return the 1st unoccupied in the list and book it automatically

        for(int i = 0 ; i < parkingSpotManager.parkingSpots.size(); i++){
            if(parkingSpotManager.parkingSpots.get(i).isAvailable){
                System.out.println("find available spot " +  i);
                spot = i;
                break;
            }
        }
        return parkingSpotManager.parkingSpots.get(spot);
    }
}
