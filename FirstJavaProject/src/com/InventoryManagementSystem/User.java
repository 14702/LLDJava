package com.InventoryManagementSystem;
import java.util.List;

public class User {
    public int id;
    public String name;
    Order order;
    Cart cart;

    User(int id, String name){
        this.id = id;
        this.name = name;
        order = new Order((int)(Math.random() * 10000) , this.id);
        cart = new Cart();
    }

    public void addProductToCart(Product product){
        this.cart.addProduct(product);
    }

}
