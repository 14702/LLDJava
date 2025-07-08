package com.InventoryManagementSystem;
import java.util.*;

public class Warehouse {
    int id;
    List<Product> warehouseProducts;

    Warehouse(int id){
        this.id = id;
        this.warehouseProducts = new ArrayList<>();
    }

    public void addProductToWarehouse(Product product){
        if(!warehouseProducts.contains(product)){
            warehouseProducts.add(product);
            System.out.println("Added Product to Warehouse " + product.name);
        } else
            System.out.println("Product Already Added to Warehouse " + product.name);
    }

    public void removeProductToWarehouse(Product product){
        if(warehouseProducts.contains(product)){
            warehouseProducts.remove(product);
            System.out.println("Removed Product to Warehouse " + product.name);
        } else
            System.out.println("Product Already removed from Warehouse " + product.name);
    }
}
