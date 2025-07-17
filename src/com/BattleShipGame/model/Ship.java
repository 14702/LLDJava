package com.BattleShipGame.model;

public class Ship {
    private final String id;
    private final String player; // "A" or "B"
    private final int x, y, width, height;
    private boolean destroyed = false;

    public Ship(String id, String player, int x, int y, int width, int height) {
        this.id = id;
        this.player = player;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public String getId() { return id; }
    public String getPlayer() { return player; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean isDestroyed() { return destroyed; }
    public void setDestroyed(boolean destroyed) { this.destroyed = destroyed; }
}
