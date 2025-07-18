package com.SnakeAndLadder.model;

import com.SnakeAndLadder.enums.ElementType;

public class LadderMove extends Move {
    Integer start;
    Integer end;

    LadderMove(ElementType elementType) {
        super(elementType);
    }

    @Override
    public Integer getPos() {
        return end;
    }

    @Override
    public Boolean setMove(Integer start, Integer end) {
        if (end <= start) {
            return false;
        } else {
            this.start = start;
            this.end = end;
        }
        return true;
    }

    @Override
    public ElementType getElementType(){
        return this.elementType;
    }

}