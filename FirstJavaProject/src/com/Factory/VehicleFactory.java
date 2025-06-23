package com.Factory;

public class VehicleFactory {

    public static Vehicle createVehicle(String type){
        if(type.isEmpty()) return null;
        else {
            switch (type){
                case "car" :return new Car();
                case "bike" : return new Bike();
                case "truck" : return new Truck();
                default: throw new IllegalArgumentException("Vehicle type is unknown " + type);
            }
        }
    }
}
