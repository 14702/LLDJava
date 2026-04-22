package com.InventoryManagementSystem.service.interfaces;

import com.InventoryManagementSystem.model.*;
import com.InventoryManagementSystem.strategy.interfaces.WarehouseStrategy;

public interface InventoryService {
    void addWarehouse(Warehouse warehouse);
    void addProductToWarehouse(int warehouseId, Product product);
    void setStrategy(WarehouseStrategy strategy);
    Order placeOrder(User user);
}
