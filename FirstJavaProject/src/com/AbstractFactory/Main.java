package com.AbstractFactory;
import java.util.Scanner; // for input

public class Main{
    public static void main (String [] args){
        // Create new factory
//        FactoryofFactory factory ;
//        Scanner sc = new Scanner(System.in);
//
//        System.out.println("Input Type of Factory ");
//        String type = sc.nextLine();
//
//        if(type.isEmpty()){
//            System.out.println("invalid factory name");
//        } else {
//            switch (type){
//                case "land" : factory = new LandVehicleFactory();
//                case "water": factory = new SeaVehicleFactory();
//            }
//        }

        FactoryofFactory factory = new LandVehicleFactory();
        Vehicle veh = factory.createVehicle("bike");
        veh.drive();
        // Create veh from those factories

        // Drive the veh
    }
}