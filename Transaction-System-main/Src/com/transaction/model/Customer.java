package com.transaction.model;

import com.transaction.interfaces.Displayable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Customer extends User implements Displayable {
    private static int nextAccountNum = 1001;
    private static int nextDepositId = 5001;
    
    private final String accountNumber;
    private double balance;
    
    public Customer(String username, String fullName, String phoneNumber, 
                    double initialBalance, String password) {
        super(username, fullName, phoneNumber, password);
        this.accountNumber = "ACC" + nextAccountNum++;
        this.balance = initialBalance;
    }
    
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    
    // ========== DEPOSIT METHODS ==========
    
    // Version 1: Basic deposit (1 parameter)
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("[OK] Deposited: $" + amount);
        } else {
            System.out.println("[ERROR] Amount must be positive!");
        }
    }
    
    // Version 2: Receive from another account (4 parameters)
    public void deposit(double amount, String fromAccount, String transactionId, String note) {
        if (amount > 0) {
            String depositId = "DEP" + nextDepositId++;
            balance += amount;
            System.out.println("\n┌─────────────────────────────────────────────────────────┐");
            System.out.println("│              RECEIVED MONEY CONFIRMATION                 │");
            System.out.println("├─────────────────────────────────────────────────────────┤");
            System.out.println("│ Deposit ID:     " + depositId);
            System.out.printf("│ Amount:         $%,.2f%n", amount);
            System.out.println("│ From Account:   " + fromAccount);
            System.out.println("│ Transaction ID: " + transactionId);
            System.out.println("│ Note:           " + note);
            System.out.printf("│ New Balance:    $%,.2f%n", balance);
            System.out.println("└─────────────────────────────────────────────────────────┘");
        } else {
            System.out.println("[ERROR] Amount must be positive!");
        }
    }
    
    // Version 3: ATM Deposit (5 parameters - different method name)
    public void depositATM(double amount, String atmId, String atmLocation, String depositType, String envelopeId) {
        if (amount > 0) {
            String depositId = "DEP" + nextDepositId++;
            balance += amount;
            System.out.println("\n┌─────────────────────────────────────────────────────────┐");
            System.out.println("│                  ATM DEPOSIT CONFIRMATION                │");
            System.out.println("├─────────────────────────────────────────────────────────┤");
            System.out.println("│ Deposit ID:     " + depositId);
            System.out.printf("│ Amount:         $%,.2f%n", amount);
            System.out.println("│ ATM ID:         " + atmId);
            System.out.println("│ ATM Location:   " + atmLocation);
            System.out.println("│ Deposit Type:   " + depositType);
            System.out.println("│ Envelope ID:    " + envelopeId);
            System.out.printf("│ New Balance:    $%,.2f%n", balance);
            System.out.println("└─────────────────────────────────────────────────────────┘");
        } else {
            System.out.println("[ERROR] Amount must be positive!");
        }
    }
    
    // Version 4: Teller Deposit (5 parameters - different method name)
    public void depositTeller(double amount, String tellerId, String tellerName, String sourceType, String customerSignature) {
        if (amount > 0) {
            String depositId = "DEP" + nextDepositId++;
            balance += amount;
            System.out.println("\n┌─────────────────────────────────────────────────────────┐");
            System.out.println("│                TELLER DEPOSIT CONFIRMATION               │");
            System.out.println("├─────────────────────────────────────────────────────────┤");
            System.out.println("│ Deposit ID:     " + depositId);
            System.out.printf("│ Amount:         $%,.2f%n", amount);
            System.out.println("│ Teller ID:      " + tellerId);
            System.out.println("│ Teller Name:    " + tellerName);
            System.out.println("│ Source Type:    " + sourceType);
            System.out.println("│ Signature:      " + customerSignature);
            System.out.printf("│ New Balance:    $%,.2f%n", balance);
            System.out.println("└─────────────────────────────────────────────────────────┘");
        } else {
            System.out.println("[ERROR] Amount must be positive!");
        }
    }
    
    // ========== WITHDRAW METHODS ==========
    
    // Version 1: Basic withdraw (1 parameter)
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("[OK] Withdrew: $" + amount);
            return true;
        }
        System.out.println("[ERROR] Insufficient balance!");
        return false;
    }
    
    // Version 2: Withdraw with reason (2 parameters)
    public boolean withdrawReason(double amount, String reason) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("\n┌─────────────────────────────────────────────────────────┐");
            System.out.println("│                  WITHDRAWAL CONFIRMATION                 │");
            System.out.println("├─────────────────────────────────────────────────────────┤");
            System.out.printf("│ Amount:         $%,.2f%n", amount);
            System.out.println("│ Reason:         " + reason);
            System.out.printf("│ New Balance:    $%,.2f%n", balance);
            System.out.println("└─────────────────────────────────────────────────────────┘");
            return true;
        }
        System.out.println("[ERROR] Insufficient balance!");
        return false;
    }
    
    // Version 3: Withdraw with receipt (2 parameters - boolean)
    public boolean withdrawReceipt(double amount, boolean printReceipt) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            if (printReceipt) {
                System.out.println("\n┌─────────────────────────────────────────────────────────┐");
                System.out.println("│                  WITHDRAWAL RECEIPT                     │");
                System.out.println("├─────────────────────────────────────────────────────────┤");
                System.out.printf("│ Amount:         $%,.2f%n", amount);
                System.out.printf("│ New Balance:    $%,.2f%n", balance);
                System.out.println("│ Date/Time:      " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                System.out.println("└─────────────────────────────────────────────────────────┘");
            } else {
                System.out.println("[OK] Withdrew: $" + amount);
            }
            return true;
        }
        System.out.println("[ERROR] Insufficient balance!");
        return false;
    }
    
    public static int getNextAccountNum() {
        return nextAccountNum;
    }
    
    @Override
    public void displayInfo() {
        System.out.println("┌─────────────────────────────────────────────────────────┐");
        System.out.println("│                   CUSTOMER INFORMATION                  │");
        System.out.println("├─────────────────────────────────────────────────────────┤");
        System.out.println("│ Account:     " + accountNumber);
        System.out.println("│ Username:    " + getUsername());
        System.out.println("│ Name:        " + getFullName());
        System.out.println("│ Phone:       " + getPhoneNumber());
        System.out.printf("│ Balance:     $%,.2f%n", balance);
        System.out.println("└─────────────────────────────────────────────────────────┘");
    }
}