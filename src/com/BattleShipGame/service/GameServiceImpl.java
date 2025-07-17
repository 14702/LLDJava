package com.BattleShipGame.service;
import com.BattleShipGame.model.Board;
import com.BattleShipGame.model.Ship;
import com.BattleShipGame.service.interfaces.GameService;
import com.BattleShipGame.strategy.interfaces.Strategy;

public class GameServiceImpl implements GameService {
    private int size;
    private Board board;
    private int shipCounterA = 1;
    private int shipCounterB = 1;
    private final Strategy strategy;

    public GameServiceImpl(Strategy strategy) {
        this.strategy = strategy;
    }

    public void initGame(int size) {
        this.size = size;
        this.board = new Board(size);
        shipCounterA = 1;
        shipCounterB = 1;
        System.out.println("Game initialized with battlefield size " + size + "x" + size);
        System.out.println("PlayerA assigned columns 0 to " + (size / 2 - 1));
        System.out.println("PlayerB assigned columns " + (size / 2) + " to " + (size - 1));
    }

    public boolean addShip(String player, int x, int y, int shipSize) {
        int leftLimit = 0;
        int rightLimit = size / 2;
        int shipColStart = x;
        int shipColEnd = x + shipSize - 1;

        if (player.equals("A")) {
            if (shipColStart < leftLimit || shipColEnd >= rightLimit) {
                System.out.println("PlayerA's ship must be in columns 0 to " + (rightLimit - 1));
                return false;
            }
        } else if (player.equals("B")) {
            if (shipColStart < rightLimit || shipColEnd >= size) {
                System.out.println("PlayerB's ship must be in columns " + rightLimit + " to " + (size - 1));
                return false;
            }
        }

        String id = "SH" + (player.equals("A") ? shipCounterA++ : shipCounterB++);
        Ship ship = new Ship(id, player, x, y, shipSize);
        boolean placed = board.placeShip(ship);
        if (placed) {
            System.out.println("Added " + player + "'s ship with id " + id + " at (" + x + "," + y + ") size " + shipSize);
        } else {
            System.out.println("Failed to add " + player + "'s ship with id " + id + " at (" + x + "," + y + ")");
        }
        return placed;
    }

    public void viewBattleField() {
        board.viewBattleField();
    }

    public void startGame() {
        boolean isATurn = true;
        while (true) {
            String player = isATurn ? "A" : "B";
            String opponent = isATurn ? "B" : "A";
            int[] shot = strategy.nextShot(size, player);
            if (player.equals("B")) {
                shot[0] = strategy.randomInRange(0, size / 2);
            } else {
                shot[0] = strategy.randomInRange(size / 2, size);
            }
            String res = board.shoot(shot[0], shot[1]);
            String msg = "Player" + player + " turn: Missile fired at (" + shot[0] + ", " + shot[1] + "). ";
            if (res.startsWith("Hit")) {
                msg += "\"Hit\".";
                String[] split = res.split("\\|");
                if (split.length > 2 && split[2].equals("Destroyed")) {
                    msg += " Player" + opponent + " ship with id \"" + split[1].substring(2) + "\" destroyed.";
                }
            } else {
                msg += "\"Miss\".";
            }
            System.out.println(msg);

            boolean opponentHasShips = board.hasShipsRemaining(opponent);
            boolean playerHasShips = board.hasShipsRemaining(player);

            if (!opponentHasShips) {
                System.out.println("Player " + player + " wins");
                break;
            }
            if (!playerHasShips) {
                System.out.println("Player " + opponent + " wins");
                break;
            }

            isATurn = !isATurn;
        }
    }
}