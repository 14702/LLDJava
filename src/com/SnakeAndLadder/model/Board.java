package com.SnakeAndLadder.model;

import java.util.List;


public class Board {
    List<Cell> cells;

    public List<Cell> getCells() {
        return cells;
    }

    public Board(List<Cell> cells) {
        this.cells = cells;
    }
}