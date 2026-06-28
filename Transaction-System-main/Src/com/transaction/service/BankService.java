package com.transaction.service;

import com.transaction.interfaces.*;
import com.transaction.model.*;
import java.util.*;

public class BankService implements Transferable, Requestable, Authenticatable, Searchable {
    
    private Map<String, Customer> customerDatabase = new HashMap<>();
    private Map<String, List<Transaction>> transactionHistory = new HashMap<>();
    private List<MoneyRequest> moneyRequests = new ArrayList<>();
    private Customer currentLoggedInCustomer = null;
    
    // ========== REGISTRATION WITH EXCEPTION HANDLING ==========
    
    public boolean registerCustomer(String username, String fullName, String phoneNumber, 
                                     double initialDeposit, String password) {
        try {
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be empty!");
            }
            if (customerDatabase.containsKey(username)) {
                throw new IllegalArgumentException("Username '" + username + "' already exists!");
            }
            if (fullName == null || fullName.trim().isEmpty()) {
                throw new IllegalArgumentException("Full name cannot be empty!");
            }
            if (phoneNumber == null || !phoneNumber.matches("\\d{9,12}")) {
                throw new IllegalArgumentException("Phone number must be 9-12 digits!");
            }
            if (initialDeposit < 0) {
                throw new IllegalArgumentException("Initial deposit cannot be negative!");
            }
            if (password == null || password.length() < 4) {
                throw new IllegalArgumentException("Password must be at least 4 characters!");
            }
            
            Customer newCustomer = new Customer(username, fullName, phoneNumber, initialDeposit, password);
            customerDatabase.put(username, newCustomer);
            transactionHistory.put(username, new ArrayList<>());
            
            System.out.println("\n[OK] ACCOUNT OPENED SUCCESSFULLY!");
            System.out.println("   Account Number: " + newCustomer.getAccountNumber());
            System.out.println("   Username: " + username);
            System.out.printf("   Initial Balance: $%,.2f%n", initialDeposit);
            return true;
            
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] Registration failed: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("[ERROR] Unexpected error during registration: " + e.getMessage());
            return false;
        }
    }
    
    // ========== AUTHENTICATABLE INTERFACE METHODS ==========
    
    @Override
    public boolean login(String username, String password) {
        try {
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be empty!");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be empty!");
            }
            
            Customer customer = customerDatabase.get(username);
            if (customer == null) {
                throw new IllegalArgumentException("Username '" + username + "' not found!");
            }
            if (customer.verifyPassword(password)) {
                currentLoggedInCustomer = customer;
                System.out.println("\n[OK] Welcome back, " + customer.getFullName() + "!");
                return true;
            } else {
                throw new IllegalArgumentException("Incorrect password!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("[ERROR] Login failed: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public void logout() {
        try {
            if (currentLoggedInCustomer != null) {
                System.out.println("\n[OK] Goodbye, " + currentLoggedInCustomer.getFullName() + "!");
                currentLoggedInCustomer = null;
            } else {
                System.out.println("[INFO] No user is currently logged in.");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Logout failed: " + e.getMessage());
        }
    }
    
    @Override
    public boolean verifyPassword(String password) {
        try {
            if (currentLoggedInCustomer == null) {
                throw new IllegalStateException("No user is logged in!");
            }
            return currentLoggedInCustomer.verifyPassword(password);
        } catch (IllegalStateException e) {
            System.out.println("[ERROR] " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public void changePassword(String oldPassword, String newPassword) {
        try {
            if (currentLoggedInCustomer == null) {
                throw new IllegalStateException("Please login first!");
            }
            currentLoggedInCustomer.changePassword(oldPassword, newPassword);
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to change password: " + e.getMessage());
        }
    }
    
    @Override
    public Customer getCurrentCustomer() {
        return currentLoggedInCustomer;
    }
    
    @Override
    public boolean isLoggedIn() {
        return currentLoggedInCustomer != null;
    }
    
    // ========== OVERLOADED SEND MONEY METHODS ==========
    
    public boolean sendMoney(String toUsername, double amount) {
        return sendMoney(toUsername, amount, "No description");
    }
    
    @Override
    public boolean sendMoney(String toUsername, double amount, String description) {
        try {
            if (currentLoggedInCustomer == null) {
                throw new IllegalStateException("Please login first!");
            }
            if (toUsername == null || toUsername.trim().isEmpty()) {
                throw new IllegalArgumentException("Recipient username cannot be empty!");
            }
            if (currentLoggedInCustomer.getUsername().equals(toUsername)) {
                throw new IllegalArgumentException("Cannot send money to yourself!");
            }
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be greater than 0!");
            }
            if (description == null) {
                description = "No description";
            }
            
            Customer recipient = customerDatabase.get(toUsername);
            if (recipient == null) {
                throw new IllegalArgumentException("Recipient '" + toUsername + "' not found!");
            }
            
            if (amount > currentLoggedInCustomer.getBalance()) {
                throw new IllegalArgumentException("Insufficient balance! Available: $" + 
                    currentLoggedInCustomer.getBalance());
            }
            
            // Process transaction
            currentLoggedInCustomer.withdraw(amount);
            recipient.deposit(amount);
            
            // Record transactions
            Transaction senderTx = new Transaction(currentLoggedInCustomer, recipient, 
                amount, description, TransactionType.SEND);
            Transaction recipientTx = new Transaction(currentLoggedInCustomer, recipient, 
                amount, description, TransactionType.RECEIVE);
            
            transactionHistory.get(currentLoggedInCustomer.getUsername()).add(senderTx);
            transactionHistory.get(recipient.getUsername()).add(recipientTx);
            
            System.out.println("\n┌─────────────────────────────────────────┐");
            System.out.println("│           TRANSACTION RECEIPT           │");
            System.out.println("├─────────────────────────────────────────┤");
            System.out.printf("│ Amount:      $%,.2f%n", amount);
            System.out.println("│ From:        " + currentLoggedInCustomer.getFullName());
            System.out.println("│ To:          " + recipient.getFullName());
            System.out.println("│ Description: " + description);
            System.out.println("│ Status:      [OK] COMPLETED");
            System.out.printf("│ New Balance: $%,.2f%n", currentLoggedInCustomer.getBalance());
            System.out.println("└─────────────────────────────────────────┘");
            return true;
            
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("[ERROR] Unexpected error: " + e.getMessage());
            return false;
        }
    }
    
    public boolean sendMoney(Customer recipient, double amount, String description) {
        return sendMoney(recipient.getUsername(), amount, description);
    }
    
    // ========== OVERLOADED REQUEST MONEY METHODS ==========
    
    public void requestMoney(String fromUsername, double amount) {
        requestMoney(fromUsername, amount, "No reason given");
    }
    
    @Override
    public void requestMoney(String fromUsername, double amount, String reason) {
        try {
            if (currentLoggedInCustomer == null) {
                throw new IllegalStateException("Please login first!");
            }
            if (fromUsername == null || fromUsername.trim().isEmpty()) {
                throw new IllegalArgumentException("Target username cannot be empty!");
            }
            if (currentLoggedInCustomer.getUsername().equals(fromUsername)) {
                throw new IllegalArgumentException("Cannot request money from yourself!");
            }
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be greater than 0!");
            }
            
            Customer target = customerDatabase.get(fromUsername);
            if (target == null) {
                throw new IllegalArgumentException("User '" + fromUsername + "' not found!");
            }
            
            MoneyRequest request = new MoneyRequest(currentLoggedInCustomer, target, amount, reason);
            moneyRequests.add(request);
            
            System.out.println("\n┌─────────────────────────────────────────┐");
            System.out.println("│           MONEY REQUEST SENT            │");
            System.out.println("├─────────────────────────────────────────┤");
            System.out.printf("│ Requested:   $%,.2f%n", amount);
            System.out.println("│ From:        " + target.getFullName());
            System.out.println("│ To:          " + currentLoggedInCustomer.getFullName());
            System.out.println("│ Reason:      " + reason);
            System.out.println("│ Request ID:  " + request.getRequestId());
            System.out.println("│ Status:      PENDING");
            System.out.println("└─────────────────────────────────────────┘");
            
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[ERROR] Unexpected error: " + e.getMessage());
        }
    }
    
    public void requestMoney(Customer fromCustomer, double amount, String reason) {
        requestMoney(fromCustomer.getUsername(), amount, reason);
    }
    
    @Override
    public boolean approveRequest(int requestId) {
        try {
            for (MoneyRequest req : moneyRequests) {
                if (req.getRequestId() == requestId) {
                    if (req.getTarget().equals(currentLoggedInCustomer)) {
                        boolean approved = req.approve();
                        if (approved) {
                            System.out.println("[OK] Request #" + requestId + " approved!");
                            return true;
                        }
                    } else {
                        throw new IllegalStateException("You cannot approve this request!");
                    }
                }
            }
            throw new IllegalArgumentException("Request #" + requestId + " not found!");
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("[ERROR] Unexpected error: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public void rejectRequest(int requestId) {
        try {
            for (MoneyRequest req : moneyRequests) {
                if (req.getRequestId() == requestId) {
                    if (req.getTarget().equals(currentLoggedInCustomer)) {
                        req.reject();
                        return;
                    } else {
                        throw new IllegalStateException("You cannot reject this request!");
                    }
                }
            }
            throw new IllegalArgumentException("Request #" + requestId + " not found!");
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[ERROR] Unexpected error: " + e.getMessage());
        }
    }
    
    @Override
    public void showPendingRequests() {
        try {
            boolean found = false;
            System.out.println("\n=== PENDING MONEY REQUESTS ===");
            for (MoneyRequest req : moneyRequests) {
                if (req.getStatus() == MoneyRequest.RequestStatus.PENDING) {
                    req.displayInfo();
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No pending requests.");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to show pending requests: " + e.getMessage());
        }
    }
    
    @Override
    public boolean receiveMoney(Customer fromCustomer, double amount, String description) {
        try {
            if (currentLoggedInCustomer == null) {
                throw new IllegalStateException("Please login first!");
            }
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be greater than 0!");
            }
            
            currentLoggedInCustomer.deposit(amount);
            Transaction tx = new Transaction(fromCustomer, currentLoggedInCustomer, 
                amount, description, TransactionType.RECEIVE);
            transactionHistory.get(currentLoggedInCustomer.getUsername()).add(tx);
            System.out.println("[OK] Received $" + amount + " from " + fromCustomer.getUsername());
            return true;
            
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("[ERROR] Unexpected error: " + e.getMessage());
            return false;
        }
    }
    
    // ========== OVERLOADED SEARCH METHODS ==========
    
    @Override
    public Customer findCustomerByUsername(String username) {
        try {
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be empty!");
            }
            Customer customer = customerDatabase.get(username);
            if (customer == null) {
                throw new IllegalArgumentException("Customer '" + username + "' not found!");
            }
            return customer;
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
            return null;
        }
    }
    
    public Customer findCustomerByUsername(Customer customer) {
        return customer;
    }
    
    @Override
    public List<Transaction> findTransactionsByCustomer(String username) {
        try {
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be empty!");
            }
            return transactionHistory.getOrDefault(username, new ArrayList<>());
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public List<Transaction> findTransactionsByCustomer(Customer customer) {
        return findTransactionsByCustomer(customer.getUsername());
    }
    
    @Override
    public List<Transaction> findTransactionsByDateRange(String startDate, String endDate) {
        List<Transaction> result = new ArrayList<>();
        try {
            if (startDate == null || endDate == null) {
                throw new IllegalArgumentException("Dates cannot be null!");
            }
            for (List<Transaction> list : transactionHistory.values()) {
                for (Transaction t : list) {
                    String txDate = t.getTimestamp().split(" ")[0];
                    if (txDate.compareTo(startDate) >= 0 && txDate.compareTo(endDate) <= 0) {
                        result.add(t);
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        return result;
    }
    
    @Override
    public List<Transaction> findTransactionsByAmountRange(double minAmount, double maxAmount) {
        List<Transaction> result = new ArrayList<>();
        try {
            if (minAmount < 0 || maxAmount < 0 || minAmount > maxAmount) {
                throw new IllegalArgumentException("Invalid amount range!");
            }
            for (List<Transaction> list : transactionHistory.values()) {
                for (Transaction t : list) {
                    if (t.getAmount() >= minAmount && t.getAmount() <= maxAmount) {
                        result.add(t);
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        return result;
    }
    
    // ========== OTHER METHODS ==========
    
    public void checkBalance() {
        try {
            if (currentLoggedInCustomer == null) {
                throw new IllegalStateException("Please login first!");
            }
            System.out.println("\n+-----------------------------------------+");
            System.out.println("|              CURRENT BALANCE             |");
            System.out.println("+-----------------------------------------+");
            System.out.println("| Account: " + currentLoggedInCustomer.getAccountNumber());
            System.out.printf("| Balance: $%,.2f%n", currentLoggedInCustomer.getBalance());
            System.out.println("+-----------------------------------------+");
        } catch (IllegalStateException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }
    
    public void showTransactionHistory() {
        try {
            if (currentLoggedInCustomer == null) {
                throw new IllegalStateException("Please login first!");
            }
            
            List<Transaction> myHistory = transactionHistory.get(currentLoggedInCustomer.getUsername());
            if (myHistory == null || myHistory.isEmpty()) {
                System.out.println("\n[EMPTY] No transactions yet.");
                return;
            }
            
            System.out.println("\n+-------------------------------------------------+");
            System.out.println("|              YOUR TRANSACTION HISTORY            |");
            System.out.println("+-------------------------------------------------+");
            for (Transaction t : myHistory) {
                System.out.println(t);
            }
        } catch (IllegalStateException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }
    
    public void showAllCustomers() {
        try {
            if (customerDatabase.isEmpty()) {
                System.out.println("[EMPTY] No customers registered.");
                return;
            }
            System.out.println("\n=== ALL BANK CUSTOMERS (" + customerDatabase.size() + " customers) ===");
            for (Customer c : customerDatabase.values()) {
                c.displayInfo();
            }
            System.out.println("\n[STATIC] Next Account Number: " + Customer.getNextAccountNum());
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to show customers: " + e.getMessage());
        }
    }
    
    public void showStatistics() {
        try {
            System.out.println("\n=== BANK STATISTICS ===");
            System.out.println("Total Customers: " + customerDatabase.size());
            System.out.println("[STATIC] Next Account Number: " + Customer.getNextAccountNum());
            System.out.println("[STATIC] Next Transaction ID: " + Transaction.getNextId());
            System.out.println("[STATIC] Next Request ID: " + MoneyRequest.getNextRequestId());
            System.out.println("Money Requests: " + moneyRequests.size());
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to show statistics: " + e.getMessage());
        }
    }
}