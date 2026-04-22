package com.PaymentGateway.router;

import com.PaymentGateway.enums.PaymentMethod;
import com.PaymentGateway.model.Bank;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.CopyOnWriteArrayList;

public class PaymentRouter implements RoutingStrategy {

    // method-specific routing: PaymentMethod -> bank for that method
    private final ConcurrentHashMap<PaymentMethod, Bank> methodRoutes = new ConcurrentHashMap<>();

    // weighted distribution: bank -> percentage (default fallback)
    private final ConcurrentHashMap<Bank, Integer> distribution = new ConcurrentHashMap<>();

    // all banks registered
    private final CopyOnWriteArrayList<Bank> banks = new CopyOnWriteArrayList<>();

    public void addBank(Bank bank) {
        banks.add(bank);
    }

    public void setMethodRoute(PaymentMethod method, Bank bank) {
        methodRoutes.put(method, bank);
    }

    public void removeMethodRoute(PaymentMethod method) {
        methodRoutes.remove(method);
    }

    public void setDistribution(Map<Bank, Integer> dist) {
        int total = dist.values().stream().mapToInt(Integer::intValue).sum();
        if (total != 100) {
            throw new IllegalArgumentException("Distribution must sum to 100, got " + total);
        }
        distribution.clear();
        distribution.putAll(dist);
    }

    @Override
    public Bank selectBank(PaymentMethod method) {
        // priority 1: method-specific route
        Bank methodBank = methodRoutes.get(method);
        if (methodBank != null) return methodBank;

        // priority 2: weighted distribution
        if (!distribution.isEmpty()) {
            return selectByWeight();
        }

        // priority 3: round-robin fallback
        if (banks.isEmpty()) {
            throw new IllegalStateException("No banks configured in router");
        }
        return banks.get(ThreadLocalRandom.current().nextInt(banks.size()));
    }

    private Bank selectByWeight() {
        int rand = ThreadLocalRandom.current().nextInt(100);
        int cumulative = 0;
        for (Map.Entry<Bank, Integer> entry : distribution.entrySet()) {
            cumulative += entry.getValue();
            if (rand < cumulative) return entry.getKey();
        }
        return distribution.keySet().iterator().next();
    }

    // Extension: dynamically switch traffic based on bank success rate
    public Bank selectBankDynamic(PaymentMethod method) {
        Bank methodBank = methodRoutes.get(method);
        if (methodBank != null) return methodBank;

        if (banks.isEmpty()) {
            throw new IllegalStateException("No banks configured in router");
        }
        return banks.stream()
                .max(Comparator.comparingDouble(Bank::getSuccessRate))
                .orElse(banks.get(0));
    }

    public Map<String, Integer> getDistribution() {
        Map<String, Integer> result = new LinkedHashMap<>();
        distribution.forEach((bank, pct) -> result.put(bank.getName(), pct));
        return result;
    }

    public Map<String, String> getMethodRoutes() {
        Map<String, String> result = new LinkedHashMap<>();
        methodRoutes.forEach((method, bank) -> result.put(method.name(), bank.getName()));
        return result;
    }

    public List<Bank> getBanks() {
        return Collections.unmodifiableList(banks);
    }
}
