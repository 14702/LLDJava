package com.InventoryManagementSystem.strategy.interfaces;

import com.InventoryManagementSystem.model.Warehouse;
import java.util.List;

public interface WarehouseStrategy {
    Warehouse selectWarehouse(List<Warehouse> warehouses);
}
