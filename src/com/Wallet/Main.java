package com.Wallet;

import java.util.Arrays;
import com.Wallet.FilterStrategy.TransactionFilterStrategyImpl.TransactionTypeFilterStrategy;
import com.Wallet.SortStrategy.TransactionSortStrategyImpl.AmountSortStrategy;
import com.Wallet.repository.PaymentData;
import com.Wallet.repository.UserData;
import com.Wallet.repository.WalletData;
import com.Wallet.enums.Ordering;
import com.Wallet.enums.PaymentMode;
import com.Wallet.enums.TransactionType;
import com.Wallet.service.interfaces.PaymentService;
import com.Wallet.service.interfaces.UserService;
import com.Wallet.service.interfaces.WalletServiceClient;
import com.Wallet.service.interfaces.WalletServiceInternal;
import com.Wallet.service.impl.PaymentServiceImpl;
import com.Wallet.service.impl.UserServiceImpl;
import com.Wallet.service.impl.WalletServiceClientImpl;
import com.Wallet.service.impl.WalletServiceInternalImpl;

public class Main {

    public static void main(String[] args) {
        UserData userData = new UserData();
        WalletData walletData = new WalletData();
        PaymentData paymentData = new PaymentData();

        WalletServiceClient walletService = new WalletServiceClientImpl(walletData);
        WalletServiceInternal walletServiceInternal = new WalletServiceInternalImpl(walletData);
        UserService userService = new UserServiceImpl(userData, walletServiceInternal);
        PaymentService paymentService = new PaymentServiceImpl(paymentData, walletServiceInternal);

        userService.registerUser("Somesh");
        userService.registerUser("Sunil");
        userService.registerUser("Ashish");

        walletService.fetchBalance("Somesh");
        paymentService.topUpWallet("Somesh", 100.0, PaymentMode.CREDIT_CARD);

        paymentService.getTransaction("Somesh", null, null, null);
        walletService.fetchBalance("Somesh");

        paymentService.payment("Somesh", "Ashish", 55.0, PaymentMode.UPI);

        walletService.fetchBalance("Somesh");
        walletService.fetchBalance("Ashish");

        paymentService.getTransaction("Somesh", null, null, null);
        paymentService.getTransaction("Ashish", null, null, null);


        paymentService.getTransaction("Somesh", new AmountSortStrategy(), Ordering.ASC, Arrays.asList(new TransactionTypeFilterStrategy(
                TransactionType.TOP_UP)));
    }
}