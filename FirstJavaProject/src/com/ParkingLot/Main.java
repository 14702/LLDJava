package com.ParkingLot;

public class Main {
    public static void main (String [] args){
        // create new parking Lot, manager, spot
        // create vehicle and enter it through entry gate
        // generate ticket, which has info of vehicle, spot and id
        // exit gate will check ticket, its id and collect payment and free the spot

        ParkingLot parkingLot = new ParkingLot(10);

        Vehicle veh1 = new Vehicle(1, VehicleType.TWO);
        Vehicle veh2 = new Vehicle(2, VehicleType.TWO);
        Vehicle veh3 = new Vehicle(3, VehicleType.TWO);
        Vehicle veh4 = new Vehicle(4, VehicleType.TWO);

        Ticket ticket1 = parkingLot.generateTicket(veh1);
        Ticket ticket2= parkingLot.generateTicket(veh2);

        parkingLot.exitVehicle(ticket1);
        parkingLot.exitVehicle(ticket2);

    }
}
