package com.SnakeAndLadder.model;

import com.SnakeAndLadder.enums.ElementType;

public abstract class Move {
    ElementType elementType;
    Move(ElementType elementType) {
        this.elementType = elementType;
    }

    abstract public Integer getPos();

    abstract public Boolean setMove(Integer start, Integer end);

    abstract public ElementType getElementType();
}