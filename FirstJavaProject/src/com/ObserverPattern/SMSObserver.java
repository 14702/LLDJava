package com.ObserverPattern;

public class SMSObserver implements Observer{
    public void notifyUser(){
        System.out.println("Notified via SMS");
    }
}
