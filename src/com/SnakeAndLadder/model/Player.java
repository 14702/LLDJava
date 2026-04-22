package com.SnakeAndLadder.model;

import com.SnakeAndLadder.enums.PlayerStatus;

public class Player {
    Integer id;
    String name;
    Integer position;
    PlayerStatus status;
    int skipTurns;

    public Player(Integer id, String name, Integer position, PlayerStatus status) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.status = status;
        this.skipTurns = 0;
    }

    public Integer getId() { return id; }
    public String getName() { return name; }
    public Integer getPosition() { return position; }
    public PlayerStatus getStatus() { return status; }
    public int getSkipTurns() { return skipTurns; }

    public void setPosition(Integer position) { this.position = position; }
    public void setStatus(PlayerStatus status) { this.status = status; }
    public void setSkipTurns(int skipTurns) { this.skipTurns = skipTurns; }

    @Override
    public String toString() {
        return name + " (pos=" + position + ", status=" + status + ")";
    }
}
