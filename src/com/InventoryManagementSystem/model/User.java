package com.InventoryManagementSystem.model;

public class User {
    private final int id;
    private final String name;
    private final Cart cart;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
        this.cart = new Cart();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public Cart getCart() { return cart; }
}
