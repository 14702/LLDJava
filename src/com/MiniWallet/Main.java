package com.MiniWallet;

import com.MiniWallet.enums.*;
import com.MiniWallet.exception.*;
import com.MiniWallet.filter.impl.*;
import com.MiniWallet.model.*;
import com.MiniWallet.offer.impl.*;
import com.MiniWallet.service.impl.*;
import com.MiniWallet.service.interfaces.*;
import com.MiniWallet.sorter.impl.*;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    static int passed = 0, failed = 0;

    static void assertTrue(boolean condition, String testName) {
        if (condition) {
            System.out.println("  PASS: " + testName);
            passed++;
        } else {
            System.out.println("  FAIL: " + testName);
            failed++;
        }
    }

    public static void main(String[] args) throws Exception {
        UserServiceImpl userService = new UserServiceImpl();
        TransactionService transactionService = new TransactionServiceImpl();
        OfferServiceImpl offerService = new OfferServiceImpl();
        WalletService walletService = new WalletServiceImpl(userService, transactionService, offerService);

        offerService.registerOffer(new FirstTransactionOffer());
        offerService.registerOffer(new EqualBalanceOffer());

        testRegisterUser(userService);
        testTopUp(walletService);
        testFetchBalance(walletService);
        testSendMoney(walletService, userService);
        testFirstTransactionOffer(walletService, userService);
        testEqualBalanceOffer(walletService, userService);
        testTransactionHistory(walletService, transactionService);
        testErrorScenarios(walletService, userService);
        testConcurrentTransfers(userService, walletService);

        System.out.println("\n========================================");
        System.out.println("Results: " + passed + " passed, " + failed + " failed");
        System.out.println("========================================");
    }

    static void testRegisterUser(UserServiceImpl svc) {
        System.out.println("\n--- Register User ---");

        User u1 = svc.registerUser("u1", "Alice", "alice@test.com");
        assertTrue(u1.getName().equals("Alice"), "Register Alice");

        svc.registerUser("u2", "Bob", "bob@test.com");
        svc.registerUser("u3", "Charlie", "charlie@test.com");
        assertTrue(svc.getUser("u2").getName().equals("Bob"), "Fetch Bob");

        try {
            svc.registerUser("u1", "Duplicate", "dup@test.com");
            assertTrue(false, "Duplicate user should throw");
        } catch (WalletException e) {
            assertTrue(e.getErrorCode() == ErrorCode.USER_ALREADY_EXISTS, "Duplicate user rejected");
        }
    }

    static void testTopUp(WalletService svc) {
        System.out.println("\n--- Top Up Wallet ---");

        svc.topUpWallet("u1", 500, PaymentSource.UPI);
        assertTrue(svc.fetchBalance("u1") == 500, "Alice balance 500 after top-up");

        svc.topUpWallet("u1", 300, PaymentSource.CREDIT_CARD);
        assertTrue(svc.fetchBalance("u1") == 800, "Alice balance 800 after second top-up");

        svc.topUpWallet("u2", 1000, PaymentSource.DEBIT_CARD);
        assertTrue(svc.fetchBalance("u2") == 1000, "Bob balance 1000");
    }

    static void testFetchBalance(WalletService svc) {
        System.out.println("\n--- Fetch Balance ---");

        assertTrue(svc.fetchBalance("u1") == 800, "Alice balance is 800");
        assertTrue(svc.fetchBalance("u3") == 0, "Charlie balance is 0 (no top-up)");
    }

    static void testSendMoney(WalletService svc, UserServiceImpl userSvc) {
        System.out.println("\n--- Send Money ---");

        double aliceBefore = svc.fetchBalance("u1");
        double bobBefore = svc.fetchBalance("u2");

        Transaction txn = svc.sendMoney("u1", "u2", 200);
        assertTrue(txn != null, "Send 200 from Alice to Bob");

        double aliceAfterDebit = aliceBefore - 200;
        double actualAlice = svc.fetchBalance("u1");
        double cashback = actualAlice - aliceAfterDebit;
        assertTrue(svc.fetchBalance("u2") == bobBefore + 200, "Bob received 200");
        System.out.println("    Alice balance: " + actualAlice + " (cashback: " + cashback + ")");
    }

    static void testFirstTransactionOffer(WalletService svc, UserServiceImpl userSvc) {
        System.out.println("\n--- First Transaction Offer (>100 → 10% cashback) ---");

        userSvc.registerUser("offer1", "OfferUser1", "o1@test.com");
        userSvc.registerUser("offer2", "OfferUser2", "o2@test.com");
        svc.topUpWallet("offer1", 500, PaymentSource.UPI);
        svc.topUpWallet("offer2", 100, PaymentSource.UPI);

        Transaction txn = svc.sendMoney("offer1", "offer2", 150);
        assertTrue(txn.getCashback() > 0, "First txn > 100 gets cashback");
        assertTrue(Math.abs(txn.getCashback() - 15.0) < 0.01,
                "Cashback is 10% of 150 = 15.0, got " + txn.getCashback());

        double expectedBalance = 500 - 150 + txn.getCashback();
        assertTrue(Math.abs(svc.fetchBalance("offer1") - expectedBalance) < 0.01,
                "OfferUser1 balance reflects cashback");

        Transaction txn2 = svc.sendMoney("offer1", "offer2", 120);
        double secondCb = txn2.getCashback();
        boolean noFirstTxnOffer = secondCb < 12.0;
        assertTrue(noFirstTxnOffer, "Second txn does NOT get first-transaction offer again");
    }

    static void testEqualBalanceOffer(WalletService svc, UserServiceImpl userSvc) {
        System.out.println("\n--- Equal Balance Offer (5% cashback) ---");

        userSvc.registerUser("eq1", "EqUser1", "eq1@test.com");
        userSvc.registerUser("eq2", "EqUser2", "eq2@test.com");
        svc.topUpWallet("eq1", 300, PaymentSource.UPI);
        svc.topUpWallet("eq2", 100, PaymentSource.UPI);

        Transaction txn = svc.sendMoney("eq1", "eq2", 100);
        double eq1Bal = svc.fetchBalance("eq1");
        double eq2Bal = svc.fetchBalance("eq2");
        System.out.println("    eq1 balance: " + eq1Bal + ", eq2 balance: " + eq2Bal);

        boolean gotEqualOffer = txn.getCashback() >= 5.0;
        assertTrue(gotEqualOffer, "Equal balance (200 each) triggers 5% cashback");
    }

    static void testTransactionHistory(WalletService svc, TransactionService txnSvc) {
        System.out.println("\n--- Transaction History (Filter + Sort) ---");

        userSvc_setup(svc);

        List<Transaction> allTxns = txnSvc.getTransactions("u1", null, null);
        assertTrue(allTxns.size() > 0, "u1 has transactions");

        List<Transaction> byDate = txnSvc.getTransactions("u1", null, new DateSorter(true));
        assertTrue(byDate.size() > 0, "Sort by date ascending works");
        if (byDate.size() > 1) {
            assertTrue(!byDate.get(0).getTimestamp().isAfter(byDate.get(1).getTimestamp()),
                    "First txn timestamp <= second (ascending)");
        }

        List<Transaction> byAmountDesc = txnSvc.getTransactions("u1", null, new AmountSorter(false));
        assertTrue(byAmountDesc.size() > 0, "Sort by amount descending works");
        if (byAmountDesc.size() > 1) {
            assertTrue(byAmountDesc.get(0).getAmount() >= byAmountDesc.get(1).getAmount(),
                    "First txn amount >= second (descending)");
        }

        List<Transaction> sendOnly = txnSvc.getTransactions("u1",
                new TransactionTypeFilter(TransactionType.SEND), null);
        for (Transaction t : sendOnly) {
            assertTrue(t.getType() == TransactionType.SEND, "Filtered txn is SEND type");
        }

        List<Transaction> withUser2 = txnSvc.getTransactions("u1",
                new UserFilter("u2"), null);
        for (Transaction t : withUser2) {
            assertTrue(t.getSenderId().equals("u2") || t.getReceiverId().equals("u2"),
                    "Filtered txn involves u2");
        }
    }

    static void txnSvc_setup(WalletService svc) {}
    static void userSvc_setup(WalletService svc) {}

    static void testErrorScenarios(WalletService svc, UserServiceImpl userSvc) {
        System.out.println("\n--- Error Scenarios ---");

        try {
            svc.topUpWallet("u1", -50, PaymentSource.UPI);
            assertTrue(false, "Negative amount should throw");
        } catch (WalletException e) {
            assertTrue(e.getErrorCode() == ErrorCode.INVALID_AMOUNT, "Negative amount rejected");
        }

        try {
            svc.topUpWallet("u1", 0, PaymentSource.UPI);
            assertTrue(false, "Zero amount should throw");
        } catch (WalletException e) {
            assertTrue(e.getErrorCode() == ErrorCode.INVALID_AMOUNT, "Zero amount rejected");
        }

        try {
            svc.sendMoney("u3", "u2", 100);
            assertTrue(false, "Insufficient balance should throw");
        } catch (WalletException e) {
            assertTrue(e.getErrorCode() == ErrorCode.INSUFFICIENT_BALANCE, "Insufficient balance rejected");
        }

        try {
            svc.sendMoney("u1", "u1", 50);
            assertTrue(false, "Self transfer should throw");
        } catch (WalletException e) {
            assertTrue(e.getErrorCode() == ErrorCode.SELF_TRANSFER, "Self transfer rejected");
        }

        try {
            userSvc.getUser("nonexistent");
            assertTrue(false, "Unknown user should throw");
        } catch (WalletException e) {
            assertTrue(e.getErrorCode() == ErrorCode.USER_NOT_FOUND, "User not found error");
        }
    }

    static void testConcurrentTransfers(UserServiceImpl userSvc, WalletService svc) throws Exception {
        System.out.println("\n--- Concurrent Transfers ---");

        userSvc.registerUser("c1", "ConcUser1", "c1@test.com");
        userSvc.registerUser("c2", "ConcUser2", "c2@test.com");
        svc.topUpWallet("c1", 1000, PaymentSource.UPI);
        svc.topUpWallet("c2", 1000, PaymentSource.UPI);

        int numThreads = 20;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        List<Future<?>> futures = new java.util.ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            final boolean direction = i % 2 == 0;
            futures.add(executor.submit(() -> {
                try {
                    latch.await();
                } catch (InterruptedException e) { return; }
                try {
                    if (direction) {
                        svc.sendMoney("c1", "c2", 50);
                    } else {
                        svc.sendMoney("c2", "c1", 50);
                    }
                    successCount.incrementAndGet();
                } catch (WalletException e) {
                    failCount.incrementAndGet();
                }
            }));
        }

        latch.countDown();
        for (Future<?> f : futures) { f.get(); }
        executor.shutdown();

        double c1Bal = svc.fetchBalance("c1");
        double c2Bal = svc.fetchBalance("c2");
        System.out.println("    c1=" + c1Bal + ", c2=" + c2Bal + ", total=" + (c1Bal + c2Bal));
        System.out.println("    Success: " + successCount.get() + ", Failed: " + failCount.get());

        assertTrue(successCount.get() + failCount.get() == numThreads,
                "All 20 threads completed");
        assertTrue(c1Bal >= 0 && c2Bal >= 0, "No negative balances");
    }
}
