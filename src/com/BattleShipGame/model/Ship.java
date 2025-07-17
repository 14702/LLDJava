package com.BattleShipGame.model;

public class Ship {
    private final String id;
    private final String player; // "A" or "B"
    private final int startX, startY, size;
    private boolean destroyed = false;

    public Ship(String id, String player, int startX, int startY, int size) {
        this.id = id;
        this.player = player;
        this.startX = startX;
        this.startY = startY;
        this.size = size;
    }

    public String getId() { return id; }
    public String getPlayer() { return player; }
    public int getStartX() { return startX; }
    public int getStartY() { return startY; }
    public int getSize() { return size; }
    public boolean isDestroyed() { return destroyed; }
    public void setDestroyed(boolean destroyed) { this.destroyed = destroyed; }
}
