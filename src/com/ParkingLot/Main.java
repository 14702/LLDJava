package com.ParkingLot;

import com.ParkingLot.enums.VehicleType;
import com.ParkingLot.model.*;
import com.ParkingLot.payment.impl.UPIPayment;
import com.ParkingLot.service.impl.ParkingServiceImpl;
import com.ParkingLot.service.interfaces.ParkingService;
import com.ParkingLot.strategy.impl.NearestParkingStrategy;

public class Main {
    public static void main(String[] args) {
        ParkingService parkingService = new ParkingServiceImpl(
                10, 20, new NearestParkingStrategy(), new UPIPayment());

        Vehicle v1 = new Vehicle(1, VehicleType.TWO);
        Vehicle v2 = new Vehicle(2, VehicleType.FOUR);

        Ticket t1 = parkingService.entry(v1, 2);
        Ticket t2 = parkingService.entry(v2, 3);

        parkingService.exit(t1);
        parkingService.exit(t2);
    }
}
