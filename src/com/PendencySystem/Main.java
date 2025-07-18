package com.PendencySystem;
import java.util.Arrays;

import com.PendencySystem.repository.interfaces.TagRepository;
import com.PendencySystem.repository.interfaces.TransactionRepository;
import com.PendencySystem.repository.impl.TagRepositoryImpl;
import com.PendencySystem.repository.impl.TransactionRepositoryImpl;
import com.PendencySystem.service.interfaces.PendencyManagementService;
import com.PendencySystem.service.impl.PendencyManagementServiceImpl;

public class Main {
    public static void main(String[] args) {
        TransactionRepository transactionRepository = TransactionRepositoryImpl.getInstance();
        TagRepository tagRepository = TagRepositoryImpl.getInstance();
        PendencyManagementService pendencyManagementService = new PendencyManagementServiceImpl(transactionRepository, tagRepository);

        // tracks Tags
        pendencyManagementService.startTracking(1112, Arrays.asList("UPI", "Karnataka", "Bangalore"));
        pendencyManagementService.startTracking(2451, Arrays.asList("UPI", "Karnataka", "Mysore"));
        pendencyManagementService.startTracking(3421, Arrays.asList("UPI", "Rajasthan", "Jaipur"));
        pendencyManagementService.startTracking(1221, Arrays.asList("Wallet", "Karnataka", "Bangalore"));
        Integer UPITagCount = pendencyManagementService.getCounts(Arrays.asList("UPI"));
        System.out.println("Total count for hierarchical tag {UPI} is " + UPITagCount);

        Integer UPIKarnatakaTagCount = pendencyManagementService.getCounts(Arrays.asList("UPI", "Karnataka"));
        System.out.println("Total count for hierarchical tag {UPI, Karnataka}  is " + UPIKarnatakaTagCount);

        Integer UPIKarnatakaBangaloreTagCount = pendencyManagementService.getCounts(Arrays.asList("UPI", "Karnataka", "Bangalore"));
        System.out.println("Total count for hierarchical tag {UPI,Karnataka, Bangalore} is " + UPIKarnatakaBangaloreTagCount);

        Integer walletTagCount = pendencyManagementService.getCounts(Arrays.asList("Wallet"));
        System.out.println("Total count for hierarchical tag {Wallet} is " + walletTagCount);

        Integer bangaloreTagCount = pendencyManagementService.getCounts(Arrays.asList("Bangalore"));
        System.out.println("Total count for hierarchical tag {Bangalore} is " + bangaloreTagCount);

        // Track more tags
        pendencyManagementService.startTracking(4221, Arrays.asList("Wallet", "Karnataka", "Bangalore"));

        // Stop tracking
        pendencyManagementService.stopTracking(1112);
        pendencyManagementService.stopTracking(2451);

        UPITagCount = pendencyManagementService.getCounts(Arrays.asList("UPI"));
        System.out.println("Total count for hierarchical tag {UPI} is " + UPITagCount);

        walletTagCount = pendencyManagementService.getCounts(Arrays.asList("Wallet"));
        System.out.println("Total count for hierarchical tag {Wallet} is " + walletTagCount);

        UPIKarnatakaBangaloreTagCount = pendencyManagementService.getCounts(Arrays.asList("UPI", "Karnataka", "Bangalore"));
        System.out.println("Total count for hierarchical tag {UPI,Karnataka, Bangalore} is " + UPIKarnatakaBangaloreTagCount);
    }
}
