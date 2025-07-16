package com.Singleton;

public class Game {
    private static Game game = new Game();
    String name;

    private Game(){
        this.name = "GTA5";
    }

    public static Game getGameInstance (){
        return game;
    }
}
