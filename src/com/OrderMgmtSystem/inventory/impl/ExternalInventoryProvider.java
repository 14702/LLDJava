package com.OrderMgmtSystem.inventory.impl;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.OrderMgmtSystem.exception.ErrorCode;
import com.OrderMgmtSystem.exception.OrderException;
import com.OrderMgmtSystem.inventory.interfaces.InventoryProvider;
import com.OrderMgmtSystem.model.InventoryItem;
import com.OrderMgmtSystem.model.OrderItem;

public class ExternalInventoryProvider implements InventoryProvider {
    private final ConcurrentHashMap<String, InventoryItem> inventory = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    public ExternalInventoryProvider() {
        // Simulating external seller's catalog
        inventory.put("EXT_PHONE", new InventoryItem("EXT_PHONE", 50, 15000));
        inventory.put("EXT_CHARGER", new InventoryItem("EXT_CHARGER", 200, 500));
        inventory.put("EXT_CASE", new InventoryItem("EXT_CASE", 300, 250));
    }

    private ReentrantLock getLock(String itemId) {
        return locks.computeIfAbsent(itemId, k -> new ReentrantLock());
    }

    @Override
    public int getAvailableQuantity(String itemId) {
        InventoryItem item = inventory.get(itemId);
        if (item == null) throw new OrderException(ErrorCode.ITEM_NOT_FOUND, "External item not found: " + itemId);
        return item.getAvailableQuantity();
    }

    @Override
    public double getPricePerUnit(String itemId) {
        InventoryItem item = inventory.get(itemId);
        if (item == null) throw new OrderException(ErrorCode.ITEM_NOT_FOUND, "External item not found: " + itemId);
        return item.getPricePerUnit();
    }

    @Override
    public void reserve(List<OrderItem> items) {
        for (OrderItem oi : items) {
            ReentrantLock lock = getLock(oi.getItemId());
            lock.lock();
            try {
                InventoryItem inv = inventory.get(oi.getItemId());
                if (inv == null) throw new OrderException(ErrorCode.ITEM_NOT_FOUND, "External item not found: " + oi.getItemId());
                if (inv.getAvailableQuantity() < oi.getQuantity()) {
                    throw new OrderException(ErrorCode.INSUFFICIENT_INVENTORY,
                            "External seller: not enough stock for " + oi.getItemId());
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