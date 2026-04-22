package com.OrderMgmtSystem.service.interfaces;

import java.util.List;

import com.OrderMgmtSystem.model.Order;
import com.OrderMgmtSystem.model.OrderState;
import com.OrderMgmtSystem.model.SellerType;
import com.OrderMgmtSystem.model.OrderItem;

public interface OrderService {
    void addItemToInventory(String itemId, int quantity, double pricePerUnit);
    int getAvailableInventory(String itemId, SellerType seller);
    Order createOrder(String customerId, List<OrderItem> items, String address, SellerType seller);
    Order updateOrder(String orderId, OrderState newState);
}