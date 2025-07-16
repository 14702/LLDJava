package com.Splitwise;

public class PercentageSplit extends Split{
    int percentage;
    PercentageSplit(User user, int amount, int percentage){
        super(user, amount);
        this.percentage = percentage;
    }
}
