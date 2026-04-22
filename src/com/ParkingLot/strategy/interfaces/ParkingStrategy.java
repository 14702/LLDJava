package com.ParkingLot.strategy.interfaces;

import com.ParkingLot.model.ParkingSpot;
import java.util.List;

public interface ParkingStrategy {
    ParkingSpot findSpot(List<ParkingSpot> spots);
}
