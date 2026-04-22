package com.InventoryManagementSystem.service.impl;

import com.InventoryManagementSystem.model.*;
import com.InventoryManagementSystem.service.interfaces.InventoryService;
import com.InventoryManagementSystem.strategy.interfaces.WarehouseStrategy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class InventoryServiceImpl implements InventoryService {

    private final CopyOnWriteArrayList<Warehouse> warehouses = new CopyOnWriteArrayList<>();
    private volatile WarehouseStrategy strategy;

    @Override
    public void addWarehouse(Warehouse warehouse) {
        warehouses.add(warehouse);
    }

    @Override
    public void addProductToWarehouse(int warehouseId, Product product) {
        for (Warehouse wh : warehouses) {
            if (wh.getId() == warehouseId) {
                if (wh.addProduct(product)) {
                    System.out.println("Added " + product.getName() + " to warehouse " + warehouseId);
                } else {
                    System.out.println(product.getName() + " already in warehouse " + warehouseId);
                }
                return;
            }
        }
        throw new IllegalArgumentException("Warehouse not found: " + warehouseId);
    }

    @Override
    public void setStrategy(WarehouseStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public Order placeOrder(User user) {
        List<Product> cartProducts = user.getCart().getProducts();
        if (cartProducts.isEmpty()) {
            System.out.println("Cart is empty");
            return null;
        }

        Warehouse warehouse = strategy.selectWarehouse(warehouses);
        warehouse.getLock().lock();
        try {
            int totalAmount = 0;
            for (Product p : cartProducts) {
                if (!warehouse.hasProduct(p.getId())) {
                    System.out.println("Warehouse doesn't have " + p.getName() + " — order failed");
                    return null;
                }
            }
            for (Product p : cartProducts) {
                warehouse.removeProduct(p.getId());
                totalAmount += p.getPrice();
            }

            Order order = new Order(user.getId(), cartProducts, totalAmount);
            order.markPaid();
            System.out.println("Order #" + order.getId() + " placed for " + user.getName()
                    + " | Amount: " + totalAmount);
            return order;
        } finally {
            warehouse.getLock().unlock();
        }
    }
}
