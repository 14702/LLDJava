package com.Singleton;

public class Main {
    public static void main(String [] args){
        Game game = Game.getGameInstance();
        System.out.println(game.name);
    }
}
