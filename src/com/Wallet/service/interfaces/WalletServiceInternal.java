package com.Wallet.service.interfaces;

import com.Wallet.model.Wallet;

// Interface Segregation for Wallet Service
public interface WalletServiceInternal {
    Wallet createWallet(String userId);
    Wallet getWalletByUserId(String userId);
}