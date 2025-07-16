package com.InventoryManagementSystem;
import java.util.*;

public class WarehouseController {
    List<Warehouse> listWarehouse;
    WarehouseStrategy warehouseStrategy;

    WarehouseController(int num){
        listWarehouse = new ArrayList<>();
        for(int i = 0 ; i < num; i++){
            listWarehouse.add(new Warehouse(i));
        }
        warehouseStrategy = new NearestWarehouse();
    }

    public Warehouse getWarehouse (int num){
        return listWarehouse.get(num); // always return last warehouse
    }

    public void addWarehouse(Warehouse warehouse){
        listWarehouse.add(warehouse);
    }
    public void removeWarehouse(Warehouse warehouse){
        listWarehouse.remove(warehouse);
    }

    public void placeOrder(Cart cart){
        Warehouse warehouse = warehouseStrategy.findWarehouse(listWarehouse);
        int orderAmount = 0 ;
        for(int i = 0 ; i < cart.productList.size(); i++){
            if(warehouse.warehouseProducts.contains(cart.productList.get(i))){
                System.out.println("Warehouse has product " + cart.productList.get(i).name);
                warehouse.warehouseProducts.remove(cart.productList.get(i));
                orderAmount += cart.productList.get(i).price;
            }
            else {
                System.out.println("Warehouse dont have inventory");
                return;
            }
        }

        Payment.makePayment(orderAmount);

    }
}
