package com.OrderMgmtSystem.service.impl;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.OrderMgmtSystem.exception.ErrorCode;
import com.OrderMgmtSystem.exception.OrderException;
import com.OrderMgmtSystem.inventory.interfaces.InventoryProvider;
import com.OrderMgmtSystem.model.Order;
import com.OrderMgmtSystem.model.OrderItem;
import com.OrderMgmtSystem.model.OrderState;
import com.OrderMgmtSystem.model.SellerType;
import com.OrderMgmtSystem.service.interfaces.OrderService;

public class OrderServiceImpl implements OrderService {
    private final InventoryProvider internalProvider;
    private final InventoryProvider externalProvider;
    private final ConcurrentHashMap<String, Order> orders = new ConcurrentHashMap<>();
    private final AtomicInteger orderCounter = new AtomicInteger(0);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final long autoCancelDelayMs;

    public OrderServiceImpl(InventoryProvider internalProvider,
                            InventoryProvider externalProvider,
                            long autoCancelDelayMs) {
        this.internalProvider = internalProvider;
        this.externalProvider = externalProvider;
        this.autoCancelDelayMs = autoCancelDelayMs;
    }

    private InventoryProvider getProvider(SellerType seller) {
        return seller == SellerType.INTERNAL ? internalProvider : externalProvider;
    }

    @Override
    public void addItemToInventory(String itemId, int quantity, double pricePerUnit) {
        internalProvider.addItem(itemId, quantity, pricePerUnit);
    }

    @Override
    public int getAvailableInventory(String itemId, SellerType seller) {
        return getProvider(seller).getAvailableQuantity(itemId);
    }

    @Override
    public Order createOrder(String customerId, List<OrderItem> items, String address, SellerType seller) {
        if (customerId == null || customerId.isEmpty()) {
            throw new OrderException(ErrorCode.INVALID_ORDER, "Customer ID is required");
        }
        if (items == null || items.isEmpty()) {
            throw new OrderException(ErrorCode.INVALID_ORDER, "Order must have at least one item");
        }
        if (address == null || address.isEmpty()) {
            throw new OrderException(ErrorCode.INVALID_ORDER, "Address is required");
        }
        for (OrderItem oi : items) {
            if (oi.getQuantity() <= 0) {
                throw new OrderException(ErrorCode.INVALID_QUANTITY, "Quantity must be positive for item: " + oi.getItemId());
            }
        }

        InventoryProvider provider = getProvider(seller);

        double totalAmount = 0;
        for (OrderItem oi : items) {
            totalAmount += provider.getPricePerUnit(oi.getItemId()) * oi.getQuantity();
        }

        provider.reserve(items);

        String orderId = "ORD-" + orderCounter.incrementAndGet();
        Order order = new Order(orderId, customerId, items, address, seller, totalAmount);
        orders.put(orderId, order);

        scheduleAutoCancel(orderId);

        return order;
    }

    @Override
    public synchronized Order updateOrder(String orderId, OrderState newState) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new OrderException(ErrorCode.ORDER_NOT_FOUND, "Order not found: " + orderId);
        }

        OrderState current = order.getState();
        InventoryProvider provider = getProvider(order.getSeller());

        switch (newState) {
            case CONFIRMED:
                if (current != OrderState.CREATED) {
                    throw new OrderException(ErrorCode.INVALID_STATE_TRANSITION,
                            "Only CREATED orders can be confirmed. Current: " + current);
                }
                provider.confirmReservation(order.getItems());
                order.setState(OrderState.CONFIRMED);
                break;

            case CANCELLED:
                if (current == OrderState.FULFILLED) {
                    throw new OrderException(ErrorCode.INVALID_STATE_TRANSITION, "Cannot cancel a fulfilled order");
                }
                if (current == OrderState.CANCELLED) {
                    throw new OrderException(ErrorCode.INVALID_STATE_TRANSITION, "Order is already cancelled");
                }
                if (current == OrderState.CREATED) {
                    provider.releaseReserved(order.getItems());
                } else if (current == OrderState.CONFIRMED) {
                    provider.releaseBlocked(order.getItems());
                }
                order.setState(OrderState.CANCELLED);
                break;

            case FULFILLED:
                if (current != OrderState.CONFIRMED) {
                    throw new OrderException(ErrorCode.INVALID_STATE_TRANSITION,
                            "Only CONFIRMED orders can be fulfilled. Current: " + current);
                }
                provider.fulfill(order.getItems());
                order.setState(OrderState.FULFILLED);
                break;

            default:
                throw new OrderException(ErrorCode.INVALID_STATE_TRANSITION, "Cannot transition to: " + newState);
        }

        return order;
    }

    private void scheduleAutoCancel(String orderId) {
        if (autoCancelDelayMs <= 0) return;
        scheduler.schedule(() -> {
            Order order = orders.get(orderId);
            if (order != null && order.getState() == OrderState.CREATED) {
                try {
                    updateOrder(orderId, OrderState.CANCELLED);
                    System.out.println("[AUTO-CANCEL] Order " + orderId + " cancelled due to timeout");
                } catch (OrderException ignored) { }
            }
        }, autoCancelDelayMs, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}