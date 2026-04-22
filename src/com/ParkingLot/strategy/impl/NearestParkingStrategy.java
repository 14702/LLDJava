package com.ParkingLot.strategy.impl;

import com.ParkingLot.model.ParkingSpot;
import com.ParkingLot.strategy.interfaces.ParkingStrategy;
import java.util.List;

public class NearestParkingStrategy implements ParkingStrategy {
    @Override
    public ParkingSpot findSpot(List<ParkingSpot> spots) {
        for (ParkingSpot spot : spots) {
            if (spot.isAvailable()) return spot;
        }
        return null;
    }
}
