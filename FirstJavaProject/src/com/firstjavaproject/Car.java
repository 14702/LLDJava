package com.firstjavaproject;

public class Car extends Vehicle{
    String model;

    Car(int odo, String model){
        super(odo);
        this.model = model;
    }

    public int getodo (){
        return super.odo;
    }
}