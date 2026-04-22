package com.OrderMgmtSystem.inventory.interfaces;

import java.util.List;
import com.OrderMgmtSystem.model.OrderItem;

public interface InventoryProvider {
    void addItem(String itemId, int quantity, double pricePerUnit);
    int getAvailableQuantity(String itemId);
    double getPricePerUnit(String itemId);
    void reserve(List<OrderItem> items);
    void confirmReservation(List<OrderItem> items);
    void releaseReserved(List<OrderItem> items);
    void releaseBlocked(List<OrderItem> items);
    void fulfill(List<OrderItem> items);
}