package com.BattleShipGame.model;
import com.BattleShipGame.enums.CellStatus;
import java.util.HashMap;

public class Board {
    private final int size;
    private final CellStatus[][] grid;
    private final String[][] shipMarkers; // holds "A-SH1", "B-SH1", etc.
    private final HashMap<String, Ship> shipMap = new HashMap<>();

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

    public boolean placeShip(Ship ship) {
        int x = ship.getX(), y = ship.getY(), w = ship.getWidth(), h = ship.getHeight();
        // Bound check
        if (x < 0 || y < 0 || x + w > size || y + h > size) return false;
        // Overlap check
        for (int i = x; i < x + w; i++)
            for (int j = y; j < y + h; j++)
                if (grid[i][j] != CellStatus.EMPTY) return false;
        // Place ship
        for (int i = x; i < x + w; i++)
            for (int j = y; j < y + h; j++) {
                grid[i][j] = CellStatus.SHIP;
                shipMarkers[i][j] = ship.getPlayer() + "-" + ship.getId();
            }
        shipMap.put(ship.getPlayer() + "-" + ship.getId(), ship);
        return true;
    }

    public String shoot(int x, int y) {
        if (grid[x][y] == CellStatus.SHIP) {
            grid[x][y] = CellStatus.HIT;
            String marker = shipMarkers[x][y];
            Ship ship = shipMap.get(marker);
            if (ship != null && isShipDestroyed(ship)) {
                ship.setDestroyed(true);
                return "Hit|" + marker + "|Destroyed";
            }
            return "Hit|" + marker;
        } else if (grid[x][y] == CellStatus.EMPTY) {
            grid[x][y] = CellStatus.MISS;
            return "Miss";
        } else {
            return "Miss"; // already hit/miss
        }
    }

    private boolean isShipDestroyed(Ship ship) {
        int x = ship.getX(), y = ship.getY(), w = ship.getWidth(), h = ship.getHeight();
        for (int i = x; i < x + w; i++)
            for (int j = y; j < y + h; j++)
                if (grid[i][j] == CellStatus.SHIP) return false;
        return true;
    }

    public boolean hasShipsRemaining(String player) {
        for (String key : shipMap.keySet()) {
            Ship ship = shipMap.get(key);
            if (ship.getPlayer().equals(player) && !ship.isDestroyed())
                return true;
        }
        return false;
    }

    public void viewBattleField() {
        System.out.println("Battlefield:");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (shipMarkers[i][j] != null && !shipMarkers[i][j].isEmpty()) {
                    System.out.print(shipMarkers[i][j] + "\t");
                } else if (grid[i][j] == CellStatus.HIT) {
                    System.out.print("X\t");
                } else if (grid[i][j] == CellStatus.MISS) {
                    System.out.print("O\t");
                } else {
                    System.out.print(".\t");
                }
            }
            System.out.println();
        }
    }
}
