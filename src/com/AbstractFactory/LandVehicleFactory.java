package com.AbstractFactory;

public class LandVehicleFactory implements FactoryofFactory{
    public Vehicle createVehicle(String type){
        if(type.isEmpty()){
            return null;
        } else {
            switch(type){
                case "bike" : return new Bike();
                case "car" : return new Car();
            }
        }
        return null;
    }
}
