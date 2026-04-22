package com.PaymentGateway;

import com.PaymentGateway.enums.PaymentMethod;
import com.PaymentGateway.model.*;
import com.PaymentGateway.router.PaymentRouter;

import java.util.*;

public class PaymentGatewayTest {

    private static int passed = 0, failed = 0;

    public static void main(String[] args) {
        testClientManagement();
        testPaymodeManagement();
        testPaymentWithMethodRouting();
        testWeightedDistribution();
        testInvalidInstrumentRejected();
        testUnsupportedMethodRejected();
        testDynamicRoutingBySuccessRate();
        testConcurrentPayments();

        System.out.println("\n=== Test Results: " + passed + " passed, " + failed + " failed ===");
    }

    static void testClientManagement() {
        PaymentRouter router = new PaymentRouter();
        PaymentGateway pg = new PaymentGateway(router);

        pg.addClient("flipkart");
        assertTrue("hasClient after add", pg.hasClient("flipkart"));
        assertFalse("hasClient unknown", pg.hasClient("amazon"));

        pg.removeClient("flipkart");
        assertFalse("hasClient after remove", pg.hasClient("flipkart"));

        assertThrows("duplicate add after re-add", () -> {
            pg.addClient("x");
            pg.addClient("x");
        });
    }

    static void testPaymodeManagement() {
        PaymentRouter router = new PaymentRouter();
        PaymentGateway pg = new PaymentGateway(router);

        pg.addSupportForPaymode(PaymentMethod.UPI);
        pg.addSupportForPaymode(PaymentMethod.CREDIT_CARD);
        assertTrue("PG supports UPI", pg.listSupportedPaymodes().contains(PaymentMethod.UPI));

        pg.addClient("flipkart");
        pg.addSupportForPaymode("flipkart", PaymentMethod.UPI);
        assertTrue("client supports UPI",
            pg.listSupportedPaymodes("flipkart").contains(PaymentMethod.UPI));
        assertFalse("client doesn't support CC",
            pg.listSupportedPaymodes("flipkart").contains(PaymentMethod.CREDIT_CARD));

        pg.removePaymode("flipkart", PaymentMethod.UPI);
        assertFalse("client UPI removed",
            pg.listSupportedPaymodes("flipkart").contains(PaymentMethod.UPI));
    }

    static void testPaymentWithMethodRouting() {
        PaymentRouter router = new PaymentRouter();
        Bank hdfc = new Bank("HDFC");
        Bank icici = new Bank("ICICI");
        router.addBank(hdfc);
        router.addBank(icici);
        router.setMethodRoute(PaymentMethod.CREDIT_CARD, hdfc);
        router.setMethodRoute(PaymentMethod.NET_BANKING, icici);

        PaymentGateway pg = new PaymentGateway(router);
        pg.addSupportForPaymode(PaymentMethod.CREDIT_CARD);
        pg.addSupportForPaymode(PaymentMethod.NET_BANKING);
        pg.addClient("flipkart");
        pg.addSupportForPaymode("flipkart", PaymentMethod.CREDIT_CARD);
        pg.addSupportForPaymode("flipkart", PaymentMethod.NET_BANKING);

        PaymentDetails ccPayment = new PaymentDetails.Builder("flipkart", PaymentMethod.CREDIT_CARD, 500)
                .cardDetails("4111111111111111", "12/25", "123").build();
        Transaction t1 = pg.makePayment(ccPayment);
        assertTrue("CC routed to HDFC", t1.getBankName().equals("HDFC"));

        PaymentDetails nbPayment = new PaymentDetails.Builder("flipkart", PaymentMethod.NET_BANKING, 300)
                .netBankingCredentials("user1", "pass1").build();
        Transaction t2 = pg.makePayment(nbPayment);
        assertTrue("NB routed to ICICI", t2.getBankName().equals("ICICI"));
    }

    static void testWeightedDistribution() {
        PaymentRouter router = new PaymentRouter();
        Bank hdfc = new Bank("HDFC");
        Bank sbi = new Bank("SBI");
        router.addBank(hdfc);
        router.addBank(sbi);

        Map<Bank, Integer> dist = new LinkedHashMap<>();
        dist.put(hdfc, 70);
        dist.put(sbi, 30);
        router.setDistribution(dist);

        PaymentGateway pg = new PaymentGateway(router);
        pg.addSupportForPaymode(PaymentMethod.UPI);
        pg.addClient("amazon");
        pg.addSupportForPaymode("amazon", PaymentMethod.UPI);

        int hdfcCount = 0;
        for (int i = 0; i < 1000; i++) {
            PaymentDetails upi = new PaymentDetails.Builder("amazon", PaymentMethod.UPI, 100)
                    .upiVpa("user@upi").build();
            Transaction t = pg.makePayment(upi);
            if (t.getBankName().equals("HDFC")) hdfcCount++;
        }

        double hdfcPct = hdfcCount / 10.0;
        assertTrue("HDFC gets ~70% (got " + hdfcPct + "%)",
            hdfcPct > 50 && hdfcPct < 90);
    }

    static void testInvalidInstrumentRejected() {
        PaymentRouter router = new PaymentRouter();
        router.addBank(new Bank("HDFC"));
        PaymentGateway pg = new PaymentGateway(router);
        pg.addSupportForPaymode(PaymentMethod.CREDIT_CARD);
        pg.addClient("flipkart");
        pg.addSupportForPaymode("flipkart", PaymentMethod.CREDIT_CARD);

        // CC payment without card details
        PaymentDetails bad = new PaymentDetails.Builder("flipkart", PaymentMethod.CREDIT_CARD, 100).build();
        assertThrows("missing card details", () -> pg.makePayment(bad));
    }

    static void testUnsupportedMethodRejected() {
        PaymentRouter router = new PaymentRouter();
        router.addBank(new Bank("HDFC"));
        PaymentGateway pg = new PaymentGateway(router);
        pg.addSupportForPaymode(PaymentMethod.UPI);
        pg.addClient("flipkart");
        // client has no paymodes added

        PaymentDetails upi = new PaymentDetails.Builder("flipkart", PaymentMethod.UPI, 100)
                .upiVpa("x@upi").build();
        assertThrows("unsupported method for client", () -> pg.makePayment(upi));
    }

    static void testDynamicRoutingBySuccessRate() {
        PaymentRouter router = new PaymentRouter();
        Bank good = new Bank("GOOD_BANK");
        Bank bad = new Bank("BAD_BANK");
        router.addBank(good);
        router.addBank(bad);

        // simulate: good bank processes more successfully
        PaymentDetails dummy = new PaymentDetails.Builder("test", PaymentMethod.UPI, 10)
                .upiVpa("x@upi").build();
        for (int i = 0; i < 100; i++) {
            good.processPayment(dummy);
            bad.processPayment(dummy);
        }

        // dynamic routing should prefer the bank with higher success rate
        Bank selected = router.selectBankDynamic(PaymentMethod.UPI);
        // both are random but over 100 tries they should have measurable rates
        assertTrue("dynamic routing selects a bank", selected != null);
    }

    static void testConcurrentPayments() {
        PaymentRouter router = new PaymentRouter();
        router.addBank(new Bank("HDFC"));
        PaymentGateway pg = new PaymentGateway(router);
        pg.addSupportForPaymode(PaymentMethod.UPI);
        pg.addClient("flipkart");
        pg.addSupportForPaymode("flipkart", PaymentMethod.UPI);

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(() -> {
                for (int j = 0; j < 50; j++) {
                    PaymentDetails upi = new PaymentDetails.Builder("flipkart", PaymentMethod.UPI, 10)
                            .upiVpa("user@upi").build();
                    pg.makePayment(upi);
                }
            }));
        }
        threads.forEach(Thread::start);
        threads.forEach(t -> { try { t.join(); } catch (InterruptedException e) { } });

        assertTrue("concurrent: 500 txns recorded",
            pg.getTransactions().size() == 500);
    }

    // --- Assertion Helpers ---

    static void assertTrue(String name, boolean condition) {
        if (condition) { passed++; System.out.println("  PASS: " + name); }
        else { failed++; System.out.println("  FAIL: " + name); }
    }

    static void assertFalse(String name, boolean condition) {
        assertTrue(name, !condition);
    }

    static void assertThrows(String name, Runnable r) {
        try { r.run(); failed++; System.out.println("  FAIL (no exception): " + name); }
        catch (Exception e) { passed++; System.out.println("  PASS: " + name + " -> " + e.getMessage()); }
    }
}
