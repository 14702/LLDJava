package com.InventoryManagementSystem.model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Warehouse {
    private final int id;
    private final String city;
    private final ConcurrentHashMap<Integer, Product> products = new ConcurrentHashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    public Warehouse(int id, String city) {
        this.id = id;
        this.city = city;
    }

    public boolean addProduct(Product product) {
        return products.putIfAbsent(product.getId(), product) == null;
    }

    public Product removeProduct(int productId) {
        return products.remove(productId);
    }

    public boolean hasProduct(int productId) {
        return products.containsKey(productId);
    }

    public int getId() { return id; }
    public String getCity() { return city; }
    public int getProductCount() { return products.size(); }

    public ReentrantLock getLock() { return lock; }
}
