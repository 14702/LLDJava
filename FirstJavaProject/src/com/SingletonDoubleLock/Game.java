package com.SingletonDoubleLock;

public class Game {

    private static volatile Game game = null;
    private Game (){

    }

    public static Game getInstance(){
        if(game == null){
            synchronized (Game.class){ // only 1 thread can access the block
                if(game == null){
                    game = new Game();
                }
            }
        }

        return game;
    }
}
// Using "volatile" keyword ensure that game is saved in RAM not in cache, so that all threads see same game instance.

// Disadvantage :
// Saving in RAM directly is usally slower than saving in cache
