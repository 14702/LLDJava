package com.PaymentGateway;

import com.PaymentGateway.enums.PaymentMethod;
import com.PaymentGateway.model.*;
import com.PaymentGateway.router.PaymentRouter;

import java.util.LinkedHashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Payment Gateway Demo ===\n");

        // 1. Setup banks
        Bank hdfc = new Bank("HDFC");
        Bank icici = new Bank("ICICI");
        Bank sbi = new Bank("SBI");

        // 2. Configure router
        PaymentRouter router = new PaymentRouter();
        router.addBank(hdfc);
        router.addBank(icici);
        router.addBank(sbi);

        // Route: CC -> HDFC, NetBanking -> ICICI
        router.setMethodRoute(PaymentMethod.CREDIT_CARD, hdfc);
        router.setMethodRoute(PaymentMethod.NET_BANKING, icici);

        // UPI/Debit use weighted distribution: HDFC 70%, SBI 30%
        Map<Bank, Integer> dist = new LinkedHashMap<>();
        dist.put(hdfc, 70);
        dist.put(sbi, 30);
        router.setDistribution(dist);

        // 3. Create Payment Gateway
        PaymentGateway pg = new PaymentGateway(router);

        // 4. Add supported payment methods to PG
        for (PaymentMethod m : PaymentMethod.values()) {
            pg.addSupportForPaymode(m);
        }

        // 5. Onboard clients
        pg.addClient("flipkart");
        pg.addClient("amazon");

        System.out.println("Has flipkart? " + pg.hasClient("flipkart"));
        System.out.println("Has myntra? " + pg.hasClient("myntra"));

        // 6. Configure client payment methods
        // Flipkart supports all methods
        for (PaymentMethod m : PaymentMethod.values()) {
            pg.addSupportForPaymode("flipkart", m);
        }
        // Amazon supports only UPI and Credit Card
        pg.addSupportForPaymode("amazon", PaymentMethod.UPI);
        pg.addSupportForPaymode("amazon", PaymentMethod.CREDIT_CARD);

        System.out.println("\nPG supported paymodes: " + pg.listSupportedPaymodes());
        System.out.println("Flipkart paymodes: " + pg.listSupportedPaymodes("flipkart"));
        System.out.println("Amazon paymodes: " + pg.listSupportedPaymodes("amazon"));

        // 7. Show routing configuration
        System.out.println("\nMethod routes: " + pg.showMethodRoutes());
        System.out.println("Distribution: " + pg.showDistribution());

        // 8. Process payments
        System.out.println("\n--- Processing Payments ---");

        // Flipkart: Credit Card -> routes to HDFC
        System.out.println("\nFlipkart CC payment:");
        pg.makePayment(new PaymentDetails.Builder("flipkart", PaymentMethod.CREDIT_CARD, 1500.0)
                .cardDetails("4111111111111111", "12/26", "789").build());

        // Flipkart: NetBanking -> routes to ICICI
        System.out.println("\nFlipkart NetBanking payment:");
        pg.makePayment(new PaymentDetails.Builder("flipkart", PaymentMethod.NET_BANKING, 2500.0)
                .netBankingCredentials("john_doe", "secret123").build());

        // Amazon: UPI -> weighted distribution
        System.out.println("\nAmazon UPI payment:");
        pg.makePayment(new PaymentDetails.Builder("amazon", PaymentMethod.UPI, 499.0)
                .upiVpa("user@oksbi").build());

        // Flipkart: Debit Card -> weighted distribution
        System.out.println("\nFlipkart Debit Card payment:");
        pg.makePayment(new PaymentDetails.Builder("flipkart", PaymentMethod.DEBIT_CARD, 999.0)
                .cardDetails("5500000000000004", "01/27", "456").build());

        // 9. Show transactions
        System.out.println("\n--- All Transactions ---");
        pg.getTransactions().forEach(System.out::println);

        System.out.println("\n--- Flipkart Transactions ---");
        pg.getTransactions("flipkart").forEach(System.out::println);

        // 10. Remove client
        pg.removeClient("amazon");
        System.out.println("\nAfter removing amazon, has amazon? " + pg.hasClient("amazon"));

        // 11. Run tests
        System.out.println("\n=== Running Tests ===");
        PaymentGatewayTest.main(args);
    }
}
