package com.SnakeAndLadder.model;

import com.SnakeAndLadder.enums.PlayerStatus;

public class Player {
    Integer id;
    Integer position;
    PlayerStatus status;

    public Player (Integer id, Integer position, PlayerStatus status) {
        this.id = id;
        this.position = position;
        this.status = status;
    }

    public Integer getId(){
        return this.id;
    }

    public Integer getPosition(){
        return this.position;
    }

    public void setPosition(Integer position){
        this.position = position;
    }

    public void setStatus(PlayerStatus status){
        this.status = status;
    }
}
