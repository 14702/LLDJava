package com.ParkingLot;
import java.util.List;

public class ExitGate {
    List<Ticket> activeTickets ;

    ExitGate(List<Ticket> activeTickets){
        this.activeTickets = activeTickets;
    }

    public void exitVehicle(Ticket ticket){
        if(!activeTickets.contains(ticket)){
            System.out.println("Invalid Ticket");
        } else {
            for(int i = 0 ; i < activeTickets.size(); i++){
                if(activeTickets.get(i).equals(ticket)){
                    System.out.println("Found active ticket");
                    // calculate price
                    // do payment
                    // free spot
                    int cost = ticket.duration * 20;
                    Payment payment = new UPIPayment();
                    payment.payTicket(cost);

                    System.out.println("Active Tickets before removing "+ activeTickets.size());
                    activeTickets.remove(ticket);
                    System.out.println("Active Tickets after removing "+ activeTickets.size());

                    ParkingSpot spot = ticket.parkingSpot;
                    spot.freeSpot();
                }
            }
        }
    }

}
