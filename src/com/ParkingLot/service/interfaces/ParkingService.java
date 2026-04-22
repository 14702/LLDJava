package com.ParkingLot.service.interfaces;

import com.ParkingLot.model.Ticket;
import com.ParkingLot.model.Vehicle;

public interface ParkingService {
    Ticket entry(Vehicle vehicle, int duration);
    void exit(Ticket ticket);
}
