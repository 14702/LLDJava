package com.PaymentGateway.router;

import com.PaymentGateway.enums.PaymentMethod;
import com.PaymentGateway.model.Bank;

// Strategy pattern — allows swapping routing logic
public interface RoutingStrategy {
    Bank selectBank(PaymentMethod method);
}
