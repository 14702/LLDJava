package com.InventoryManagementSystem.model;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Order {
    private static final AtomicInteger ID_GEN = new AtomicInteger(0);

    private final int id;
    private final int userId;
    private final List<Product> products;
    private final int totalAmount;
    private boolean paid;

    public Order(int userId, List<Product> products, int totalAmount) {
        this.id = ID_GEN.incrementAndGet();
        this.userId = userId;
        this.products = products;
        this.totalAmount = totalAmount;
    }

    public void markPaid() { this.paid = true; }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public List<Product> getProducts() { return products; }
    public int getTotalAmount() { return totalAmount; }
    public boolean isPaid() { return paid; }
}
