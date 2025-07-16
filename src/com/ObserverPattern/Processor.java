package com.ObserverPattern;

import java.util.ArrayList;
import java.util.List;

public class Processor {
    List<Observer> obsList = new ArrayList<>();

    public void addToList(Observer observer){
        obsList.add(observer);
    }

    public void removeFromList (Observer observer){
        obsList.remove(observer);
    }

    public void notifyall(){
        for(int i = 0 ; i < obsList.size(); i++){
            obsList.get(i).notifyUser();
        }
    }

}
