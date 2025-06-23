package com.AbstractFactory;

public class SeaVehicleFactory implements FactoryofFactory{
    public Vehicle createVehicle(String type){
        if(type.isEmpty()){
            return null;
        } else {
           /* switch(type){
                case "yatch" : return new Yatch();
                case "boat" : return new Boat();
            }*/
        }
        return null;
    }
}
