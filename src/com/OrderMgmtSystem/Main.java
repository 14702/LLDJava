package com.OrderMgmtSystem;

import java.util.Arrays;
import java.util.Collections;

import com.OrderMgmtSystem.exception.OrderException;
import com.OrderMgmtSystem.inventory.impl.ExternalInventoryProvider;
import com.OrderMgmtSystem.inventory.impl.InternalInventoryProvider;
import com.OrderMgmtSystem.model.Order;
import com.OrderMgmtSystem.model.OrderItem;
import com.OrderMgmtSystem.model.OrderState;
import com.OrderMgmtSystem.model.SellerType;
import com.OrderMgmtSystem.service.impl.OrderServiceImpl;

public class Main {
    static int passed = 0, failed = 0;

    static void assertEquals(Object expected, Object actual, String testName) {
        if (expected.equals(actual)) {
            System.out.println("  PASS: " + testName);
            passed++;
        } else {
            System.out.println("  FAIL: " + testName + " | Expected: " + expected + ", Got: " + actual);
            failed++;
        }
    }

    static void expectException(Runnable r, String testName) {
        try {
            r.run();
            System.out.println("  FAIL: " + testName + " | Expected exception but none thrown");
            failed++;
        } catch (OrderException e) {
            System.out.println("  PASS: " + testName + " (" + e.getErrorCode() + ")");
            passed++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        InternalInventoryProvider internalProvider = new InternalInventoryProvider();
        ExternalInventoryProvider externalProvider = new ExternalInventoryProvider();
        OrderServiceImpl service = new OrderServiceImpl(internalProvider, externalProvider, 2000);

        System.out.println("=== 1. Add Items to Internal Inventory ===");
        service.addItemToInventory("LAPTOP", 10, 50000);
        service.addItemToInventory("MOUSE", 100, 500);
        service.addItemToInventory("KEYBOARD", 50, 1500);
        assertEquals(10, service.getAvailableInventory("LAPTOP", SellerType.INTERNAL), "LAPTOP has 10 units");
        assertEquals(100, service.getAvailableInventory("MOUSE", SellerType.INTERNAL), "MOUSE has 100 units");

        System.out.println("\n=== 2. Add More Quantity to Existing Item ===");
        service.addItemToInventory("LAPTOP", 5, 50000);
        assertEquals(15, service.getAvailableInventory("LAPTOP", SellerType.INTERNAL), "LAPTOP now has 15 units");

        System.out.println("\n=== 3. Get External Seller Inventory ===");
        assertEquals(50, service.getAvailableInventory("EXT_PHONE", SellerType.EXTERNAL), "External phone has 50 units");

        System.out.println("\n=== 4. Create Order (Internal) - Reserves Inventory ===");
        Order order1 = service.createOrder("CUST-1",
                Arrays.asList(new OrderItem("LAPTOP", 2), new OrderItem("MOUSE", 3)),
                "123 Main St", SellerType.INTERNAL);
        assertEquals(OrderState.CREATED, order1.getState(), "Order state is CREATED");
        assertEquals(101500.0, order1.getTotalAmount(), "Total = 2*50000 + 3*500 = 101500");
        assertEquals(13, service.getAvailableInventory("LAPTOP", SellerType.INTERNAL), "LAPTOP available after reserve: 13");
        assertEquals(97, service.getAvailableInventory("MOUSE", SellerType.INTERNAL), "MOUSE available after reserve: 97");

        System.out.println("\n=== 5. Confirm Order - Reserves Become Blocked ===");
        service.updateOrder(order1.getOrderId(), OrderState.CONFIRMED);
        assertEquals(OrderState.CONFIRMED, order1.getState(), "Order confirmed");
        assertEquals(13, service.getAvailableInventory("LAPTOP", SellerType.INTERNAL), "LAPTOP still 13 available (now blocked)");

        System.out.println("\n=== 6. Fulfill Order ===");
        service.updateOrder(order1.getOrderId(), OrderState.FULFILLED);
        assertEquals(OrderState.FULFILLED, order1.getState(), "Order fulfilled");
        assertEquals(13, service.getAvailableInventory("LAPTOP", SellerType.INTERNAL), "LAPTOP 13 available after fulfillment");

        System.out.println("\n=== 7. Create & Cancel Order - Inventory Released ===");
        Order order2 = service.createOrder("CUST-2",
                Collections.singletonList(new OrderItem("KEYBOARD", 5)),
                "456 Oak Ave", SellerType.INTERNAL);
        assertEquals(45, service.getAvailableInventory("KEYBOARD", SellerType.INTERNAL), "KEYBOARD reserved: 45 available");
        service.updateOrder(order2.getOrderId(), OrderState.CANCELLED);
        assertEquals(OrderState.CANCELLED, order2.getState(), "Order cancelled");
        assertEquals(50, service.getAvailableInventory("KEYBOARD", SellerType.INTERNAL), "KEYBOARD restored: 50 available");

        System.out.println("\n=== 8. Cancel Confirmed Order - Blocked Inventory Released ===");
        Order order3 = service.createOrder("CUST-3",
                Collections.singletonList(new OrderItem("MOUSE", 10)),
                "789 Pine Rd", SellerType.INTERNAL);
        service.updateOrder(order3.getOrderId(), OrderState.CONFIRMED);
        assertEquals(87, service.getAvailableInventory("MOUSE", SellerType.INTERNAL), "MOUSE 87 available (10 blocked)");
        service.updateOrder(order3.getOrderId(), OrderState.CANCELLED);
        assertEquals(97, service.getAvailableInventory("MOUSE", SellerType.INTERNAL), "MOUSE 97 available after cancel");

        System.out.println("\n=== 9. External Seller Order ===");
        Order order4 = service.createOrder("CUST-4",
                Arrays.asList(new OrderItem("EXT_PHONE", 2), new OrderItem("EXT_CHARGER", 5)),
                "101 Elm St", SellerType.EXTERNAL);
        assertEquals(OrderState.CREATED, order4.getState(), "External order created");
        assertEquals(48, service.getAvailableInventory("EXT_PHONE", SellerType.EXTERNAL), "EXT_PHONE 48 available");
        service.updateOrder(order4.getOrderId(), OrderState.CONFIRMED);
        service.updateOrder(order4.getOrderId(), OrderState.FULFILLED);
        assertEquals(OrderState.FULFILLED, order4.getState(), "External order fulfilled");

        System.out.println("\n=== 10. Validation Errors ===");
        expectException(() -> service.createOrder("", Collections.singletonList(new OrderItem("LAPTOP", 1)),
                "addr", SellerType.INTERNAL), "Empty customer ID");
        expectException(() -> service.createOrder("C1", Collections.emptyList(),
                "addr", SellerType.INTERNAL), "Empty items list");
        expectException(() -> service.createOrder("C1", Collections.singletonList(new OrderItem("LAPTOP", 1)),
                "", SellerType.INTERNAL), "Empty address");
        expectException(() -> service.createOrder("C1", Collections.singletonList(new OrderItem("LAPTOP", 100)),
                "addr", SellerType.INTERNAL), "Insufficient inventory");
        expectException(() -> service.createOrder("C1", Collections.singletonList(new OrderItem("NO_SUCH", 1)),
                "addr", SellerType.INTERNAL), "Item not found");

        System.out.println("\n=== 11. Invalid State Transitions ===");
        Order order5 = service.createOrder("CUST-5",
                Collections.singletonList(new OrderItem("LAPTOP", 1)),
                "addr", SellerType.INTERNAL);
        expectException(() -> service.updateOrder(order5.getOrderId(), OrderState.FULFILLED),
                "Cannot fulfill CREATED order");
        service.updateOrder(order5.getOrderId(), OrderState.CONFIRMED);
        expectException(() -> service.updateOrder(order5.getOrderId(), OrderState.CONFIRMED),
                "Cannot confirm already CONFIRMED order");
        service.updateOrder(order5.getOrderId(), OrderState.FULFILLED);
        expectException(() -> service.updateOrder(order5.getOrderId(), OrderState.CANCELLED),
                "Cannot cancel FULFILLED order");

        System.out.println("\n=== 12. Auto-Cancel (2 second timeout) ===");
        Order order6 = service.createOrder("CUST-6",
                Collections.singletonList(new OrderItem("MOUSE", 5)),
                "auto-cancel test", SellerType.INTERNAL);
        int availBefore = service.getAvailableInventory("MOUSE", SellerType.INTERNAL);
        System.out.println("  Waiting 3 seconds for auto-cancel...");
        Thread.sleep(3000);
        assertEquals(OrderState.CANCELLED, order6.getState(), "Order auto-cancelled after timeout");
        assertEquals(availBefore + 5, service.getAvailableInventory("MOUSE", SellerType.INTERNAL),
                "Inventory released after auto-cancel");

        service.shutdown();

        System.out.println("\n========================================");
        System.out.println("Results: " + passed + " passed, " + failed + " failed");
    }
}