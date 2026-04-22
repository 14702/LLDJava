package com.SnakeAndLadder.model;

import com.SnakeAndLadder.enums.ElementType;

public class CrocodileMove extends Move {
    private Integer end;

    public CrocodileMove(ElementType elementType) {
        super(elementType);
    }

    @Override
    public Integer getPos() { return end; }

    @Override
    public Boolean setMove(Integer start, Integer end) {
        this.end = Math.max(start - 5, 0);
        return true;
    }

    @Override
    public ElementType getElementType() { return elementType; }
}
