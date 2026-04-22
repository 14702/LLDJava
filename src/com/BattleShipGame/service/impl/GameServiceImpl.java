package com.BattleShipGame.service.impl;

import com.BattleShipGame.model.Board;
import com.BattleShipGame.model.Ship;
import com.BattleShipGame.service.interfaces.GameService;
import com.BattleShipGame.strategy.interfaces.Strategy;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class GameServiceImpl implements GameService {
    private int size;
    private Board board;
    private final AtomicInteger shipCounterA = new AtomicInteger(0);
    private final AtomicInteger shipCounterB = new AtomicInteger(0);
    private final Strategy strategyA;
    private final Strategy strategyB;
    private final ReentrantLock lock = new ReentrantLock();

    public GameServiceImpl(Strategy strategyA, Strategy strategyB) {
        this.strategyA = strategyA;
        this.strategyB = strategyB;
    }

    @Override
    public void initGame(int size) {
        lock.lock();
        try {
            if (size < 2 || size % 2 != 0)
                throw new IllegalArgumentException("Board size must be a positive even number, got: " + size);
            this.size = size;
            this.board = new Board(size);
            shipCounterA.set(0);
            shipCounterB.set(0);
            System.out.println("Game initialized with battlefield size " + size + "x" + size);
            System.out.println("PlayerA assigned columns 0 to " + (size / 2 - 1));
            System.out.println("PlayerB assigned columns " + (size / 2) + " to " + (size - 1));
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean addShip(String player, int x, int y, int shipSize) {
        lock.lock();
        try {
            if (board == null) throw new IllegalStateException("Game not initialized. Call initGame() first.");
            if (!player.equals("A") && !player.equals("B"))
                throw new IllegalArgumentException("Invalid player: " + player + ". Must be 'A' or 'B'.");
            if (shipSize <= 0)
                throw new IllegalArgumentException("Ship size must be positive, got: " + shipSize);

            int half = size / 2;
            if (player.equals("A")) {
                if (x < 0 || x + shipSize > half) {
                    System.out.println("PlayerA's ship must fit in columns 0 to " + (half - 1));
                    return false;
                }
            } else {
                if (x < half || x + shipSize > size) {
                    System.out.println("PlayerB's ship must fit in columns " + half + " to " + (size - 1));
                    return false;
                }
            }
            if (y < 0 || y + shipSize > size) {
                System.out.println("Ship must fit within rows 0 to " + (size - 1));
                return false;
            }

            AtomicInteger counter = player.equals("A") ? shipCounterA : shipCounterB;
            int num = counter.incrementAndGet();
            String id = "SH" + num;
            Ship ship = new Ship(id, player, x, y, shipSize);
            boolean placed = board.placeShip(ship);
            if (placed) {
                System.out.println("Added " + player + "'s ship " + id + " at (" + x + "," + y + ") size " + shipSize);
            } else {
                counter.decrementAndGet();
                System.out.println("Failed to place " + player + "'s ship at (" + x + "," + y + ") — overlap or out of bounds");
            }
            return placed;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void viewBattleField() {
        if (board != null) board.viewBattleField();
    }

    @Override
    public void startGame() {
        if (board == null) throw new IllegalStateException("Game not initialized. Call initGame() first.");
        int fleetA = shipCounterA.get();
        int fleetB = shipCounterB.get();
        if (fleetA == 0 || fleetB == 0)
            throw new IllegalStateException("Both players need at least one ship to start.");
        if (fleetA != fleetB)
            throw new IllegalStateException("Fleet mismatch: PlayerA has " + fleetA + " ships, PlayerB has " + fleetB + ". Must be equal.");

        int half = size / 2;
        boolean isATurn = true;
        boolean aExhausted = false, bExhausted = false;

        while (true) {
            String player = isATurn ? "A" : "B";
            String opponent = isATurn ? "B" : "A";

            int[] shot;
            if (player.equals("A")) {
                shot = strategyA.nextShot(half, size, 0, size);
            } else {
                shot = strategyB.nextShot(0, half, 0, size);
            }

            if (shot == null) {
                if (player.equals("A")) aExhausted = true;
                else bExhausted = true;
                if (aExhausted && bExhausted) {
                    System.out.println("Draw! Both players exhausted all coordinates.");
                    break;
                }
                isATurn = !isATurn;
                continue;
            }

            String res = board.shoot(shot[0], shot[1]);
            if (res.equals("AlreadyFired") || res.equals("OutOfBounds")) {
                continue;
            }

            String msg = "Player" + player + " turn: Missile fired at (" + shot[0] + ", " + shot[1] + "). ";
            if (res.startsWith("Hit")) {
                msg += "\"Hit\".";
                String[] split = res.split("\\|");
                if (split.length > 2 && split[2].equals("Destroyed")) {
                    msg += " Player" + opponent + "'s ship with id \"" + split[1].substring(2) + "\" destroyed.";
                }
            } else {
                msg += "\"Miss\".";
            }
            System.out.println(msg);

            if (!board.hasShipsRemaining(opponent)) {
                System.out.println("Player" + player + " wins! All of Player" + opponent + "'s ships have been destroyed.");
                break;
            }

            isATurn = !isATurn;
        }
    }
}
