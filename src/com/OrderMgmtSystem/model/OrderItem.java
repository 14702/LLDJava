package com.OrderMgmtSystem.model;

public class OrderItem {
    private final String itemId;
    private final int quantity;

    public OrderItem(String itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public String getItemId() { return itemId; }
    public int getQuantity() { return quantity; }
}