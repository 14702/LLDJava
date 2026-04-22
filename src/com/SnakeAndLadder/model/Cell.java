package com.SnakeAndLadder.model;

import com.SnakeAndLadder.enums.ElementType;

public class Cell {
    private Move move;

    public void setMove(Integer start, Integer end, ElementType type) {
        switch (type) {
            case SNAKE:     this.move = new SnakeMove(type); break;
            case LADDER:    this.move = new LadderMove(type); break;
            case CROCODILE: this.move = new CrocodileMove(type); break;
            case MINE:      this.move = new MineMove(type); break;
            default:        this.move = null; break;
        }
        if (this.move != null)
            this.move.setMove(start, end);
    }

    public Move getMove(){
        return this.move;
    }

}