package com.AbstractFactory;
import java.util.Scanner; // for input
// Create all the vehicle under one level of abstraction.
// For factory create new Factory interface then select concrete factory based on selection. From that selected factory create veh
// You need to create interface factoryoffactory so that u can have handle of which ever concrete child factory is created

public class Main{
    public static void main (String [] args){
        // Create new factory
        FactoryofFactory factory = null; // init abstract handle
        Scanner sc = new Scanner(System.in);

        System.out.println("Input Type of Factory ");
        String type = sc.nextLine();

        if(type.isEmpty()){
            System.out.println("invalid factory name");
        } else {
            switch (type){
                case "land" :
                    factory = new LandVehicleFactory();
                    break;
                case "water":
                    factory = new SeaVehicleFactory();
                    break;
                default :
                    System.out.println("Unknown factory type");
                    return;
            }
        }

        Vehicle veh = factory.createVehicle("bike");
        veh.drive();
        // Create veh from those factories

        // Drive the veh
    }
}