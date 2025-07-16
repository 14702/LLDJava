package com.Hackathon.src.enums;

public enum Difficulty {
    EASY(1),
    MEDIUM(2),
    HARD(3);

    private final int level;

    Difficulty(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
