package com.Factory;

public class Main{
    public static void main (String [] args){
        // Factory is main class and is sort of utlility class,
        Vehicle veh1 = VehicleFactory.createVehicle("car");
        Vehicle veh2 = VehicleFactory.createVehicle("bike");
        Vehicle veh3 = VehicleFactory.createVehicle("truck");
        //Vehicle veh4 = VehicleFactory.createVehicle("tank");

        veh1.drive();
        veh2.drive();
        veh3.drive();
    }
}