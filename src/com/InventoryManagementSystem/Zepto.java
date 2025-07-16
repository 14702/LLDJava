package com.InventoryManagementSystem;

public class Zepto {
    WarehouseController warehouseController;
    UserController userController;

    Zepto(int num){
        warehouseController = new WarehouseController(num);
        userController = new UserController();
    }

}
