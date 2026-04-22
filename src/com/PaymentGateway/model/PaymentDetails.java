package com.PaymentGateway.model;

import com.PaymentGateway.enums.PaymentMethod;

public class PaymentDetails {
    private final String clientId;
    private final PaymentMethod method;
    private final double amount;
    private final String cardNumber;
    private final String cardExpiry;
    private final String cvv;
    private final String upiVpa;
    private final String netBankingUser;
    private final String netBankingPassword;

    private PaymentDetails(Builder builder) {
        this.clientId = builder.clientId;
        this.method = builder.method;
        this.amount = builder.amount;
        this.cardNumber = builder.cardNumber;
        this.cardExpiry = builder.cardExpiry;
        this.cvv = builder.cvv;
        this.upiVpa = builder.upiVpa;
        this.netBankingUser = builder.netBankingUser;
        this.netBankingPassword = builder.netBankingPassword;
    }

    public String getClientId() { return clientId; }
    public PaymentMethod getMethod() { return method; }
    public double getAmount() { return amount; }
    public String getCardNumber() { return cardNumber; }
    public String getCardExpiry() { return cardExpiry; }
    public String getCvv() { return cvv; }
    public String getUpiVpa() { return upiVpa; }
    public String getNetBankingUser() { return netBankingUser; }
    public String getNetBankingPassword() { return netBankingPassword; }

    public boolean hasValidInstrumentDetails() {
        switch (method) {
            case CREDIT_CARD:
            case DEBIT_CARD:
                return cardNumber != null && cardExpiry != null && cvv != null;
            case UPI:
                return upiVpa != null;
            case NET_BANKING:
                return netBankingUser != null && netBankingPassword != null;
            default:
                return false;
        }
    }

    @Override
    public String toString() {
        return "PaymentDetails{client='" + clientId + "', method=" + method + ", amount=" + amount + "}";
    }

    public static class Builder {
        private final String clientId;
        private final PaymentMethod method;
        private final double amount;
        private String cardNumber, cardExpiry, cvv;
        private String upiVpa;
        private String netBankingUser, netBankingPassword;

        public Builder(String clientId, PaymentMethod method, double amount) {
            this.clientId = clientId;
            this.method = method;
            this.amount = amount;
        }

        public Builder cardDetails(String number, String expiry, String cvv) {
            this.cardNumber = number;
            this.cardExpiry = expiry;
            this.cvv = cvv;
            return this;
        }

        public Builder upiVpa(String vpa) {
            this.upiVpa = vpa;
            return this;
        }

        public Builder netBankingCredentials(String user, String password) {
            this.netBankingUser = user;
            this.netBankingPassword = password;
            return this;
        }

        public PaymentDetails build() {
            return new PaymentDetails(this);
        }
    }
}
