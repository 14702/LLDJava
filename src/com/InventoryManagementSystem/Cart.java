package com.InventoryManagementSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cart {
    //HashMap< Integer , Product > productMap;
    public List <Product> productList ;

    Cart(){
        //productMap = new HashMap<>();
        productList = new ArrayList<>();

    }

    public void addProduct(Product product){
        if(!productList.contains(product)){
            productList.add(product);
            System.out.println("added product " + product.name);
        }
    }

    //  similarly add for remove product

}
