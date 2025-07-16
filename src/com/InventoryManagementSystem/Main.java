package com.InventoryManagementSystem;

public class Main {
    public static void main (String [] args){
        // create new system
        // create 1 warehouse
        // add 2 products to warehouse
        // add WH strategy
        // Create 1 user and add 2 products to cart
        // create order and complete payment

        Zepto zepto = new Zepto(1); // create 1 WH
        Product pepsi = new Product(1, "pepsi", 100);
        Product coke = new Product(2, "coke", 200);

        Warehouse warehouse0 = zepto.warehouseController.getWarehouse(0);
        warehouse0.addProductToWarehouse(pepsi);
        warehouse0.addProductToWarehouse(coke);

        User user1 = new User(1, "Somesh");
        user1.cart.addProduct(pepsi);
        user1.cart.addProduct(coke);

        zepto.warehouseController.placeOrder(user1.cart);
        zepto.warehouseController.placeOrder(user1.cart);

        // Expansion
        // add item count for each product
        // Add multiple warehouse list and add these warehouse to diff city (use HashMap)
        // Implement other strategy (nearest, cheapest)
        //
    }
}
