package com.MiniWallet.service.impl;

import com.MiniWallet.model.Transaction;
import com.MiniWallet.model.Wallet;
import com.MiniWallet.offer.interfaces.Offer;
import com.MiniWallet.service.interfaces.OfferService;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class OfferServiceImpl implements OfferService {
    private final List<Offer> offers = new CopyOnWriteArrayList<>();

    @Override
    public void registerOffer(Offer offer) {
        offers.add(offer);
    }

    @Override
    public double applyOffers(Transaction transaction, Wallet senderWallet, Wallet receiverWallet) {
        double totalCashback = 0;
        for (Offer offer : offers) {
            if (offer.isEligible(transaction, senderWallet, receiverWallet)) {
                double cashback = offer.calculateCashback(transaction, senderWallet, receiverWallet);
                totalCashback += cashback;
                System.out.println("    Offer applied: " + offer.getName() +
                        " → cashback ₹" + String.format("%.2f", cashback));
            }
        }
        return totalCashback;
    }
}
