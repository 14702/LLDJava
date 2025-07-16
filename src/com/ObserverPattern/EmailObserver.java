package com.ObserverPattern;

public class EmailObserver implements Observer{
    public void notifyUser(){
        System.out.println("Notified via email");
    }
}
