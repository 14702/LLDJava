package com.ObserverPattern;

public class Main {
    public static void main (String [] args){
        // It all about observer class(email, sms...) and its one method during notify. Rest adding, removing and notifying (invoke notify of all observers) can be done using one Processor class
        Processor processor = new Processor();
        EmailObserver emailObserver = new EmailObserver();
        SMSObserver smsObserver = new SMSObserver();

        processor.addToList(emailObserver);
        processor.addToList(smsObserver);
        processor.notifyall();

        processor.removeFromList(smsObserver);

        System.out.println("Removed SMS notifier-----");
        processor.notifyall();

    }
}
