package com.OrderMgmtSystem.model;

public class InventoryItem {
    private final String itemId;
    private int totalQuantity;
    private int reservedQuantity;
    private int blockedQuantity;
    private double pricePerUnit;

    public InventoryItem(String itemId, int totalQuantity, double pricePerUnit) {
        this.itemId = itemId;
        this.totalQuantity = totalQuantity;
        this.pricePerUnit = pricePerUnit;
    }

    public String getItemId() { return itemId; }
    public int getTotalQuantity() { return totalQuantity; }
    public int getReservedQuantity() { return reservedQuantity; }
    public int getBlockedQuantity() { return blockedQuantity; }
    public double getPricePerUnit() { return pricePerUnit; }

    public int getAvailableQuantity() {
        return totalQuantity - reservedQuantity - blockedQuantity;
    }

    public void addQuantity(int qty) { this.totalQuantity += qty; }
    public void reserve(int qty) { this.reservedQuantity += qty; }
    public void block(int qty) { this.blockedQuantity += qty; }
    public void releaseReserved(int qty) { this.reservedQuantity -= qty; }
    public void releaseBlocked(int qty) { this.blockedQuantity -= qty; }

    public void fulfill(int qty) {
        this.blockedQuantity -= qty;
        this.totalQuantity -= qty;
    }

    public void confirmReservation(int qty) {
        this.reservedQuantity -= qty;
        this.blockedQuantity += qty;
    }
}