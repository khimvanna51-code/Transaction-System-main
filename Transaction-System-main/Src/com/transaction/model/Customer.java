package com.transaction.model;

import com.transaction.interfaces.Displayable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Customer extends User implements Displayable {
    private static int nextAccountNum = 1001;
    private static int nextDepositId = 5001;

    private final String accountNumber;
    private double balance;

    // Constructor
    public Customer(String username, String fullName, String phoneNumber, String accountNumber, double balance) {
        super(username, fullName, phoneNumber);
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }

    // ========== BASIC DEPOSIT (Needed for BankService) ==========
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("[OK] Deposited: $" + amount);
        } else {
            System.out.println("[ERROR] Amount must be positive!");
        }
    }

    // ========== OVERLOADED DEPOSIT 1: Receive from another account ==========
    // FIXED: Renamed from depositReceive to deposit
    public void deposit(double amount, String fromAccount, String transactionId, String note) {
        if (amount > 0) {
            String depositId = "DEP" + nextDepositId++;
            balance += amount;
            System.out.println("\n┌─────────────────────────────────────────────────────────┐");
            System.out.println("│              RECEIVED MONEY CONFIRMATION                │");
            System.清.println("├─────────────────────────────────────────────────────────┤");
            System.out.println("│ Deposit ID:     " + depositId);
            System.out.printf("│ Amount:          $%,.2f%n", amount);
            System.out.println("│ From Account:   " + fromAccount);
            System.out.println("│ Transaction ID: " + transactionId);
            System.out.println("│ Note:            " + note);
            System.out.printf("│ New Balance:     $%,.2f%n", balance);
            System.out.println("└─────────────────────────────────────────────────────────┘");
        } else {
            System.out.println("[ERROR] Amount must be positive!");
        }
    }
    
    // ========== OVERLOADED DEPOSIT 2: ATM Deposit ==========
    // FIXED: Renamed from depositATM to deposit
    public void deposit(double amount, String atmId, String atmLocation, String depositType, String envelopeId) {
        if (amount > 0) {
            String depositId = "DEP" + nextDepositId++;
            balance += amount;
            System.out.println("\n┌─────────────────────────────────────────────────────────┐");
            System.out.println("│                  ATM DEPOSIT CONFIRMATION               │");
            System.out.println("├─────────────────────────────────────────────────────────┤");
            System.out.println("│ Deposit ID:     " + depositId);
            System.out.printf("│ Amount:          $%,.2f%n", amount);
            System.out.println("│ ATM ID:         " + atmId);
            System.out.println("│ ATM Location:   " + atmLocation);
            System.out.println("│ Deposit Type:   " + depositType);
            System.out.println("│ Envelope ID:    " + envelopeId);
            System.out.printf("│ New Balance:     $%,.2f%n", balance);
            System.out.println("└─────────────────────────────────────────────────────────┘");
        } else {
            System.out.println("[ERROR] Amount must be positive!");
        }
    }
    
    // ========== OVERLOADED DEPOSIT 3: Teller Deposit ==========
    // FIXED: Renamed from depositTeller to deposit
    public void deposit(double amount, String tellerId, String tellerName, String sourceType, String customerSignature) {
        if (amount > 0) {
            String depositId = "DEP" + nextDepositId++;
            balance += amount;
            System.out.println("\n┌─────────────────────────────────────────────────────────┐");
            System.out.println("│                 TELLER DEPOSIT CONFIRMATION               │");
            System.out.println("├─────────────────────────────────────────────────────────┤");
            System.out.println("│ Deposit ID:     " + depositId);
            System.out.printf("│ Amount:          $%,.2f%n", amount);
            System.out.println("│ Teller ID:      " + tellerId);
            System.out.println("│ Teller Name:    " + tellerName);
            System.out.println("│ Source Type:    " + sourceType);
            System.out.println("│ Signature:      " + customerSignature);
            System.out.printf("│ New Balance:     $%,.2f%n", balance);
            System.out.println("└─────────────────────────────────────────────────────────┘");
        } else {
            System.out.println("[ERROR] Amount must be positive!");
        }
    }
    
    // ========== BASIC WITHDRAW ==========
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("[OK] Withdrew: $" + amount);
            return true;
        }
        System.out.println("[ERROR] Insufficient balance!");
        return false;
    }
    
    // ========== OVERLOADED WITHDRAW 2: With reason ==========
    // FIXED: Renamed from withdrawReason to withdraw
    public boolean withdraw(double amount, String reason) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("\n┌─────────────────────────────────────────────────────────┐");
            System.out.println("│                  WITHDRAWAL CONFIRMATION                │");
            System.out.println("├─────────────────────────────────────────────────────────┤");
            System.out.printf("│ Amount:          $%,.2f%n", amount);
            System.out.println("│ Reason:          " + reason);
            System.out.printf("│ New Balance:     $%,.2f%n", balance);
            System.out.println("└─────────────────────────────────────────────────────────┘");
            return true;
        }
        System.out.println("[ERROR] Insufficient balance!");
        return false;
    }
    
    // ========== OVERLOADED WITHDRAW 3: With receipt ==========
    // FIXED: Renamed from withdrawReceipt to withdraw
    public boolean withdraw(double amount, boolean printReceipt) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            if (printReceipt) {
                System.out.println("\n┌─────────────────────────────────────────────────────────┐");
                System.out.println("│                  WITHDRAWAL RECEIPT                     │");
                System.out.println("├─────────────────────────────────────────────────────────┤");
                System.out.printf("│ Amount:          $%,.2f%n", amount);
                System.out.printf("│ New Balance:     $%,.2f%n", balance);
                System.out.println("│ Date/Time:       " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                System.out.println("└─────────────────────────────────────────────────────────┘");
            } else {
                System.out.println("[OK] Withdrew: $" + amount);
            }
            return true;
        }
        System.out.println("[ERROR] Insufficient balance!");
        return false;
    }

    @Override
    public void displayInfo() {
        System.out.println("┌─────────────────────────────────────────────────────────┐");
        System.out.println("│                    CUSTOMER INFORMATION                 │");
        System.out.println("├─────────────────────────────────────────────────────────┤");
        System.out.println("│ Account:     " + accountNumber);
        System.out.println("│ Username:    " + getUsername());
        System.out.println("│ Name:        " + getFullName());
        System.out.println("│ Phone:       " + getPhoneNumber());
        System.out.printf("│ Balance:      $%,.2f%n", balance);
        System.out.println("└─────────────────────────────────────────────────────────┘");
    }
}
