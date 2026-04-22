package com.InventoryManagementSystem.strategy.impl;

import com.InventoryManagementSystem.model.Warehouse;
import com.InventoryManagementSystem.strategy.interfaces.WarehouseStrategy;
import java.util.List;

public class CheapestWarehouseStrategy implements WarehouseStrategy {
    @Override
    public Warehouse selectWarehouse(List<Warehouse> warehouses) {
        if (warehouses.isEmpty()) throw new IllegalStateException("No warehouses available");
        return warehouses.get(warehouses.size() - 1);
    }
}
