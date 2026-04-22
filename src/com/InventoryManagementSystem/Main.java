package com.InventoryManagementSystem;

import com.InventoryManagementSystem.model.*;
import com.InventoryManagementSystem.service.impl.InventoryServiceImpl;
import com.InventoryManagementSystem.service.interfaces.InventoryService;
import com.InventoryManagementSystem.strategy.impl.NearestWarehouseStrategy;

public class Main {
    public static void main(String[] args) {
        InventoryService service = new InventoryServiceImpl();
        service.setStrategy(new NearestWarehouseStrategy());

        Warehouse wh = new Warehouse(0, "Mumbai");
        service.addWarehouse(wh);

        Product pepsi = new Product(1, "Pepsi", 100);
        Product coke = new Product(2, "Coke", 200);
        service.addProductToWarehouse(0, pepsi);
        service.addProductToWarehouse(0, coke);

        User user = new User(1, "Somesh");
        user.getCart().addProduct(pepsi);
        user.getCart().addProduct(coke);

        Order order1 = service.placeOrder(user);
        System.out.println("Order paid: " + (order1 != null && order1.isPaid()));

        Order order2 = service.placeOrder(user);
        System.out.println("Second order (no inventory): " + (order2 == null ? "failed as expected" : "unexpected success"));
    }
}
