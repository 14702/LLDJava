package com.PaymentGateway;

import com.PaymentGateway.enums.PaymentMethod;
import com.PaymentGateway.enums.TransactionStatus;
import com.PaymentGateway.model.*;
import com.PaymentGateway.router.PaymentRouter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

// Facade pattern — single entry point for all PG operations
public class PaymentGateway {

    private final ConcurrentHashMap<String, Client> clients = new ConcurrentHashMap<>();
    private final Set<PaymentMethod> supportedMethods = ConcurrentHashMap.newKeySet();
    private final PaymentRouter router;
    private final List<Transaction> transactions = Collections.synchronizedList(new ArrayList<>());

    public PaymentGateway(PaymentRouter router) {
        this.router = router;
    }

    // --- Client Management ---

    public void addClient(String clientId) {
        if (clients.containsKey(clientId)) {
            throw new IllegalArgumentException("Client already exists: " + clientId);
        }
        clients.put(clientId, new Client(clientId));
    }

    public void removeClient(String clientId) {
        if (clients.remove(clientId) == null) {
            throw new IllegalArgumentException("Client not found: " + clientId);
        }
    }

    public boolean hasClient(String clientId) {
        return clients.containsKey(clientId);
    }

    // --- Paymode Management ---

    public void addSupportForPaymode(PaymentMethod method) {
        supportedMethods.add(method);
    }

    public void addSupportForPaymode(String clientId, PaymentMethod method) {
        if (!supportedMethods.contains(method)) {
            throw new IllegalArgumentException("Payment method " + method + " not supported by PG");
        }
        getClient(clientId).addPaymentMethod(method);
    }

    public void removePaymode(PaymentMethod method) {
        supportedMethods.remove(method);
    }

    public void removePaymode(String clientId, PaymentMethod method) {
        getClient(clientId).removePaymentMethod(method);
    }

    public Set<PaymentMethod> listSupportedPaymodes() {
        return Collections.unmodifiableSet(supportedMethods);
    }

    public Set<PaymentMethod> listSupportedPaymodes(String clientId) {
        return getClient(clientId).getSupportedMethods();
    }

    // --- Distribution ---

    public Map<String, Integer> showDistribution() {
        return router.getDistribution();
    }

    public Map<String, String> showMethodRoutes() {
        return router.getMethodRoutes();
    }

    // --- Payment Processing ---

    public Transaction makePayment(PaymentDetails details) {
        Client client = getClient(details.getClientId());

        if (!client.supportsMethod(details.getMethod())) {
            throw new IllegalArgumentException(
                "Client " + details.getClientId() + " does not support " + details.getMethod());
        }

        if (!details.hasValidInstrumentDetails()) {
            throw new IllegalArgumentException(
                "Invalid instrument details for " + details.getMethod());
        }

        Bank bank = router.selectBank(details.getMethod());
        TransactionStatus status = bank.processPayment(details);

        Transaction txn = new Transaction(
            details.getClientId(), bank.getName(), details.getMethod(),
            details.getAmount(), status);
        transactions.add(txn);

        System.out.println("  -> " + txn);
        return txn;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public List<Transaction> getTransactions(String clientId) {
        synchronized (transactions) {
            return transactions.stream()
                    .filter(t -> t.getClientId().equals(clientId))
                    .collect(Collectors.toList());
        }
    }

    private Client getClient(String clientId) {
        Client client = clients.get(clientId);
        if (client == null) {
            throw new IllegalArgumentException("Client not found: " + clientId);
        }
        return client;
    }
}
