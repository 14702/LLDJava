package com.BattleShipGame.model;

import com.BattleShipGame.enums.CellStatus;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Board {
    private final int size;
    private final CellStatus[][] grid;
    private final String[][] shipMarkers;
    private final ConcurrentHashMap<String, Ship> shipMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> shipHitCount = new ConcurrentHashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    public Board(int size) {
        this.size = size;
        this.grid = new CellStatus[size][size];
        this.shipMarkers = new String[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                grid[i][j] = CellStatus.EMPTY;
                shipMarkers[i][j] = "";
            }
    }

    public int getSize() {
        return size;
    }

    public boolean placeShip(Ship ship) {
        lock.lock();
        try {
            int x = ship.getStartX(), y = ship.getStartY(), s = ship.getSize();
            if (x < 0 || y < 0 || x + s > size || y + s > size) return false;
            for (int i = x; i < x + s; i++)
                for (int j = y; j < y + s; j++)
                    if (grid[i][j] != CellStatus.EMPTY) return false;
            String key = ship.getPlayer() + "-" + ship.getId();
            for (int i = x; i < x + s; i++)
                for (int j = y; j < y + s; j++) {
                    grid[i][j] = CellStatus.SHIP;
                    shipMarkers[i][j] = key;
                }
            shipMap.put(key, ship);
            shipHitCount.put(key, 0);
            return true;
        } finally {
            lock.unlock();
        }
    }

    public String shoot(int x, int y) {
        lock.lock();
        try {
            if (x < 0 || x >= size || y < 0 || y >= size) return "OutOfBounds";
            if (grid[x][y] == CellStatus.HIT || grid[x][y] == CellStatus.MISS) return "AlreadyFired";

            if (grid[x][y] == CellStatus.SHIP) {
                grid[x][y] = CellStatus.HIT;
                String marker = shipMarkers[x][y];
                Ship ship = shipMap.get(marker);
                int hits = shipHitCount.merge(marker, 1, Integer::sum);
                int totalCells = ship.getSize() * ship.getSize();
                if (hits >= totalCells) {
                    ship.setDestroyed(true);
                    return "Hit|" + marker + "|Destroyed";
                }
                return "Hit|" + marker;
            } else {
                grid[x][y] = CellStatus.MISS;
                return "Miss";
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean hasShipsRemaining(String player) {
        lock.lock();
        try {
            for (Ship ship : shipMap.values())
                if (ship.getPlayer().equals(player) && !ship.isDestroyed())
                    return true;
            return false;
        } finally {
            lock.unlock();
        }
    }

    public void viewBattleField() {
        lock.lock();
        try {
            System.out.println("Battlefield:");
            for (int j = 0; j < size; j++) {
                for (int i = 0; i < size; i++) {
                    if (shipMarkers[i][j] != null && !shipMarkers[i][j].isEmpty()
                            && grid[i][j] == CellStatus.SHIP) {
                        System.out.printf("%-8s", shipMarkers[i][j]);
                    } else if (grid[i][j] == CellStatus.HIT) {
                        System.out.printf("%-8s", "X");
                    } else if (grid[i][j] == CellStatus.MISS) {
                        System.out.printf("%-8s", "O");
                    } else {
                        System.out.printf("%-8s", ".");
                    }
                }
                System.out.println();
            }
        } finally {
            lock.unlock();
        }
    }
}
