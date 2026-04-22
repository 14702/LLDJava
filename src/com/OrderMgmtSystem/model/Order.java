package com.OrderMgmtSystem.model;

import java.util.List;

public class Order {
    private final String orderId;
    private final String customerId;
    private final List<OrderItem> items;
    private final String address;
    private final SellerType seller;
    private final double totalAmount;
    private final long createdAt;
    private OrderState state;

    public Order(String orderId, String customerId, List<OrderItem> items,
                 String address, SellerType seller, double totalAmount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = items;
        this.address = address;
        this.seller = seller;
        this.totalAmount = totalAmount;
        this.state = OrderState.CREATED;
        this.createdAt = System.currentTimeMillis();
    }

    public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public List<OrderItem> getItems() { return items; }
    public String getAddress() { return address; }
    public SellerType getSeller() { return seller; }
    public double getTotalAmount() { return totalAmount; }
    public OrderState getState() { return state; }
    public long getCreatedAt() { return createdAt; }

    public void setState(OrderState state) { this.state = state; }
}