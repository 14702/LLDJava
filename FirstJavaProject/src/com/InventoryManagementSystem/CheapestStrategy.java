package com.InventoryManagementSystem;

import java.util.List;

public class CheapestStrategy implements WarehouseStrategy {
    public Warehouse findWarehouse(List<Warehouse> warehouselist){
        // some logic to return particular WH
        return warehouselist.get(0);
    }
}
