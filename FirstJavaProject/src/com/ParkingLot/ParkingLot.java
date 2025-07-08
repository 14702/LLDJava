package com.ParkingLot;
import java.util.ArrayList;
import java.util.List;

public class ParkingLot {
    ParkingSpotManager parkingSpotManager;
    List<Ticket> activeTickets = new ArrayList<>();
    EntryGate entryGate;
    ExitGate exitGate;
    ParkingStrategy parkingStrategy;


    ParkingLot(int total){
        this.parkingSpotManager = new ParkingSpotManager();
        parkingSpotManager.intializeSpots(total);
        parkingStrategy = new NearestParkingStrategy();
        entryGate = new EntryGate(parkingStrategy, parkingSpotManager);
        exitGate = new ExitGate(activeTickets);

    }

    public Ticket generateTicket (Vehicle vehicle){
        ParkingSpot spot = entryGate.entryVehicle(vehicle);
        int ticketId = Ticket.generateTicket();

        Ticket ticket = new Ticket(ticketId, 2, vehicle, spot);
        this.activeTickets.add(ticket);
        System.out.println("total active tickets are "+ activeTickets.size());
        return ticket;
    }

    public void exitVehicle(Ticket ticket){
        exitGate.exitVehicle(ticket);
        System.out.println("total active tickets after EXIT are "+ activeTickets.size());
    }
}
