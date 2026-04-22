package com.MiniWallet.service.interfaces;

import com.MiniWallet.model.Transaction;
import com.MiniWallet.model.Wallet;
import com.MiniWallet.offer.interfaces.Offer;

public interface OfferService {
    void registerOffer(Offer offer);
    double applyOffers(Transaction transaction, Wallet senderWallet, Wallet receiverWallet);
}
