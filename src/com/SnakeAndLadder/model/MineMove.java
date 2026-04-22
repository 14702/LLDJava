package com.SnakeAndLadder.model;

import com.SnakeAndLadder.enums.ElementType;

public class MineMove extends Move {
    private Integer pos;

    public MineMove(ElementType elementType) {
        super(elementType);
    }

    @Override
    public Integer getPos() { return pos; }

    @Override
    public Boolean setMove(Integer start, Integer end) {
        this.pos = start;
        return true;
    }

    @Override
    public ElementType getElementType() { return elementType; }
}
