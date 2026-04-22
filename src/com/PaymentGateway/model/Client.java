package com.PaymentGateway.model;

import com.PaymentGateway.enums.PaymentMethod;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Client {
    private final String clientId;
    private final Set<PaymentMethod> supportedMethods = ConcurrentHashMap.newKeySet();

    public Client(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() { return clientId; }

    public void addPaymentMethod(PaymentMethod method) {
        supportedMethods.add(method);
    }

    public void removePaymentMethod(PaymentMethod method) {
        supportedMethods.remove(method);
    }

    public boolean supportsMethod(PaymentMethod method) {
        return supportedMethods.contains(method);
    }

    public Set<PaymentMethod> getSupportedMethods() {
        return Collections.unmodifiableSet(supportedMethods);
    }

    @Override
    public String toString() {
        return "Client[" + clientId + ", methods=" + supportedMethods + "]";
    }
}
