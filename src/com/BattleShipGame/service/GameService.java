package com.BattleShipGame.service;
import com.BattleShipGame.model.Board;
import com.BattleShipGame.model.Ship;
import com.BattleShipGame.strategy.RandomStrategy;
import java.util.ArrayList;
import java.util.List;

public class GameService {
    private int size;
    private Board board;
    private List<Ship> playerAShips = new ArrayList<>();
    private List<Ship> playerBShips = new ArrayList<>();
    private int shipCounterA = 1;
    private int shipCounterB = 1;

    public void initGame(int size) {
        this.size = size;
        this.board = new Board(size);
        playerAShips.clear();
        playerBShips.clear();
        shipCounterA = 1;
        shipCounterB = 1;
        System.out.println("Game initialized with battlefield size " + size + "x" + size);
        System.out.println("PlayerA assigned columns 0 to " + (size / 2 - 1));
        System.out.println("PlayerB assigned columns " + (size / 2) + " to " + (size - 1));
    }

    public boolean addShip(String player, int x, int y, int width, int height) {
        String id = "SH" + (player.equals("A") ? shipCounterA++ : shipCounterB++);
        Ship ship = new Ship(id, player, x, y, width, height);
        boolean placed = board.placeShip(ship);
        if (placed) {
            if (player.equals("A")) playerAShips.add(ship);
            else playerBShips.add(ship);
            System.out.println("Added " + player + "'s ship with id " + id + " at (" + x + "," + y + ") size " + width + "x" + height);
        } else {
            System.out.println("Failed to add " + player + "'s ship with id " + id + " at (" + x + "," + y + ")");
        }
        return placed;
    }

    public void viewBattleField() {
        board.viewBattleField();
    }

    public void startGame() {
        RandomStrategy strategy = new RandomStrategy();
        boolean isATurn = true;
        int turnCount = 1;
        while (board.hasShipsRemaining("A") && board.hasShipsRemaining("B")) {
            String player = isATurn ? "A" : "B";
            int[] shot;
            if (player.equals("A")) {
                shot = strategy.nextShot(size / 2); // PlayerA only shoots in left half
            } else {
                shot = strategy.nextShot(size / 2);
                shot[0] += size / 2; // PlayerB only shoots in right half
            }
            String res = board.shoot(shot[0], shot[1]);
            String msg = "Player" + player + "'s turn: Missile fired at (" + shot[0] + ", " + shot[1] + "). ";
            if (res.startsWith("Hit")) {
                msg += "\"Hit\".";
                String[] split = res.split("\\|");
                if (split.length > 2 && split[2].equals("Destroyed")) {
                    msg += " Player" + (player.equals("A") ? "B" : "A") + "'s ship with id \"" + split[1].substring(2) + "\" destroyed.";
                }
            } else {
                msg += "\"Miss\".";
            }
            System.out.println(msg);
            isATurn = !isATurn;
            turnCount++;
        }
        if (!board.hasShipsRemaining("A"))
            System.out.println("Player B wins!");
        else
            System.out.println("Player A wins!");
    }
}
