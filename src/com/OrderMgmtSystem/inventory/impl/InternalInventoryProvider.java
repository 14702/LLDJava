package com.OrderMgmtSystem.inventory.impl;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.OrderMgmtSystem.exception.ErrorCode;
import com.OrderMgmtSystem.exception.OrderException;
import com.OrderMgmtSystem.inventory.interfaces.InventoryProvider;
import com.OrderMgmtSystem.model.InventoryItem;
import com.OrderMgmtSystem.model.OrderItem;

public class InternalInventoryProvider implements InventoryProvider {
    private final ConcurrentHashMap<String, InventoryItem> inventory = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    private ReentrantLock getLock(String itemId) {
        return locks.computeIfAbsent(itemId, k -> new ReentrantLock());
    }

    @Override
    public void addItem(String itemId, int quantity, double pricePerUnit) {
        if (quantity <= 0) {
            throw new OrderException(ErrorCode.INVALID_QUANTITY, "Quantity must be positive");
        }
        ReentrantLock lock = getLock(itemId);
        lock.lock();
        try {
            InventoryItem item = inventory.get(itemId);
            if (item == null) {
                inventory.put(itemId, new InventoryItem(itemId, quantity, pricePerUnit));
            } else {
                item.addQuantity(quantity);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int getAvailableQuantity(String itemId) {
        InventoryItem item = inventory.get(itemId);
        if (item == null) throw new OrderException(ErrorCode.ITEM_NOT_FOUND, "Item not found: " + itemId);
        return item.getAvailableQuantity();
    }

    @Override
    public double getPricePerUnit(String itemId) {
        InventoryItem item = inventory.get(itemId);
        if (item == null) throw new OrderException(ErrorCode.ITEM_NOT_FOUND, "Item not found: " + itemId);
        return item.getPricePerUnit();
    }

    @Override
    public void reserve(List<OrderItem> items) {
        for (OrderItem oi : items) {
            ReentrantLock lock = getLock(oi.getItemId());
            lock.lock();
            try {
                InventoryItem inv = inventory.get(oi.getItemId());
                if (inv == null) throw new OrderException(ErrorCode.ITEM_NOT_FOUND, "Item not found: " + oi.getItemId());
                if (inv.getAvailableQuantity() < oi.getQuantity()) {
                    throw new OrderException(ErrorCode.INSUFFICIENT_INVENTORY,
                            "Not enough stock for " + oi.getItemId() + ". Available: " + inv.getAvailableQuantity());
                }
                inv.reserve(oi.getQuantity());
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public void confirmReservation(List<OrderItem> items) {
        for (OrderItem oi : items) {
            ReentrantLock lock = getLock(oi.getItemId());
            lock.lock();
            try {
                inventory.get(oi.getItemId()).confirmReservation(oi.getQuantity());
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public void releaseReserved(List<OrderItem> items) {
        for (OrderItem oi : items) {
            ReentrantLock lock = getLock(oi.getItemId());
            lock.lock();
            try {
                inventory.get(oi.getItemId()).releaseReserved(oi.getQuantity());
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public void releaseBlocked(List<OrderItem> items) {
        for (OrderItem oi : items) {
            ReentrantLock lock = getLock(oi.getItemId());
            lock.lock();
            try {
                inventory.get(oi.getItemId()).releaseBlocked(oi.getQuantity());
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public void fulfill(List<OrderItem> items) {
        for (OrderItem oi : items) {
            ReentrantLock lock = getLock(oi.getItemId());
            lock.lock();
            try {
                inventory.get(oi.getItemId()).fulfill(oi.getQuantity());
            } finally {
                lock.unlock();
            }
        }
    }
}