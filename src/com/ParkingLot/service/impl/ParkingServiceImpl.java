package com.ParkingLot.service.impl;

import com.ParkingLot.model.*;
import com.ParkingLot.payment.interfaces.PaymentMethod;
import com.ParkingLot.service.interfaces.ParkingService;
import com.ParkingLot.strategy.interfaces.ParkingStrategy;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ParkingServiceImpl implements ParkingService {

    private final CopyOnWriteArrayList<ParkingSpot> spots = new CopyOnWriteArrayList<>();
    private final ConcurrentHashMap<Integer, Ticket> activeTickets = new ConcurrentHashMap<>();
    private final ParkingStrategy strategy;
    private final PaymentMethod paymentMethod;

    public ParkingServiceImpl(int totalSpots, int chargePerHour,
                              ParkingStrategy strategy, PaymentMethod paymentMethod) {
        this.strategy = strategy;
        this.paymentMethod = paymentMethod;
        for (int i = 0; i < totalSpots; i++) {
            spots.add(new ParkingSpot(i, chargePerHour));
        }
    }

    @Override
    public synchronized Ticket entry(Vehicle vehicle, int duration) {
        ParkingSpot spot = strategy.findSpot(spots);
        if (spot == null) {
            System.out.println("No spots available");
            return null;
        }
        spot.book(vehicle);
        Ticket ticket = new Ticket(duration, vehicle, spot);
        activeTickets.put(ticket.getId(), ticket);
        System.out.println("Ticket #" + ticket.getId() + " | Spot " + spot.getId()
                + " | Vehicle " + vehicle.getNum());
        return ticket;
    }

    @Override
    public synchronized void exit(Ticket ticket) {
        if (!activeTickets.containsKey(ticket.getId())) {
            System.out.println("Invalid ticket #" + ticket.getId());
            return;
        }
        paymentMethod.pay(ticket.getCost());
        ticket.getSpot().free();
        activeTickets.remove(ticket.getId());
        System.out.println("Vehicle " + ticket.getVehicle().getNum() + " exited | Spot "
                + ticket.getSpot().getId() + " freed | Active: " + activeTickets.size());
    }

    public int getActiveCount() { return activeTickets.size(); }
    public List<ParkingSpot> getSpots() { return spots; }
}
