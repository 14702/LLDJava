package com.PendencySystem;

import java.util.Arrays;

import com.PendencySystem.repository.impl.TagRepositoryImpl;
import com.PendencySystem.repository.impl.TransactionRepositoryImpl;
import com.PendencySystem.service.interfaces.PendencyManagementService;
import com.PendencySystem.service.impl.PendencyManagementServiceImpl;

public class Main {
    public static void main(String[] args) {
        PendencyManagementService service = new PendencyManagementServiceImpl(
                new TransactionRepositoryImpl(), new TagRepositoryImpl());

        System.out.println("=== Pendency System Demo ===\n");

        service.startTracking(1112, Arrays.asList("UPI", "Karnataka", "Bangalore"));
        service.startTracking(2451, Arrays.asList("UPI", "Karnataka", "Mysore"));
        service.startTracking(3421, Arrays.asList("UPI", "Rajasthan", "Jaipur"));
        service.startTracking(1221, Arrays.asList("Wallet", "Karnataka", "Bangalore"));

        System.out.println("getCounts([UPI]) = " + service.getCounts(Arrays.asList("UPI")));                              // 3
        System.out.println("getCounts([UPI, Karnataka]) = " + service.getCounts(Arrays.asList("UPI", "Karnataka")));       // 2
        System.out.println("getCounts([UPI, Karnataka, Bangalore]) = " + service.getCounts(Arrays.asList("UPI", "Karnataka", "Bangalore"))); // 1
        System.out.println("getCounts([Wallet]) = " + service.getCounts(Arrays.asList("Wallet")));                        // 1
        System.out.println("getCounts([Bangalore]) = " + service.getCounts(Arrays.asList("Bangalore")));                  // 0 (not valid hierarchy)

        service.startTracking(4221, Arrays.asList("Wallet", "Karnataka", "Bangalore"));
        service.stopTracking(1112);
        service.stopTracking(2451);

        System.out.println("\nAfter start(4221) + stop(1112,2451):");
        System.out.println("getCounts([UPI]) = " + service.getCounts(Arrays.asList("UPI")));                              // 1
        System.out.println("getCounts([Wallet]) = " + service.getCounts(Arrays.asList("Wallet")));                        // 2
        System.out.println("getCounts([UPI, Karnataka, Bangalore]) = " + service.getCounts(Arrays.asList("UPI", "Karnataka", "Bangalore"))); // 0

        // Generic use case: restaurant orders
        System.out.println("\n=== Restaurant Orders Demo ===\n");

        PendencyManagementService orderService = new PendencyManagementServiceImpl(
                new TransactionRepositoryImpl(), new TagRepositoryImpl());

        orderService.startTracking(1, Arrays.asList("Mumbai", "PizzaHut", "Italian", "Margherita"));
        orderService.startTracking(2, Arrays.asList("Mumbai", "PizzaHut", "Italian", "Pepperoni"));
        orderService.startTracking(3, Arrays.asList("Mumbai", "Dominos", "Italian", "Farmhouse"));
        orderService.startTracking(4, Arrays.asList("Delhi", "PizzaHut", "Italian", "Margherita"));

        System.out.println("Pending in Mumbai = " + orderService.getCounts(Arrays.asList("Mumbai")));                    // 3
        System.out.println("Pending at Mumbai PizzaHut = " + orderService.getCounts(Arrays.asList("Mumbai", "PizzaHut"))); // 2
        System.out.println("Pending at Delhi = " + orderService.getCounts(Arrays.asList("Delhi")));                       // 1

        System.out.println("\n=== Running Tests ===\n");
        PendencySystemTest.main(args);
    }
}
