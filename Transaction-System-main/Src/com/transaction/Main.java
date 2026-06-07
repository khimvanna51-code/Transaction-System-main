package com.transaction;

import com.transaction.model.Customer;
import com.transaction.service.BankService;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static BankService bank = new BankService();
    
    public static void main(String[] args) {
        System.out.println("\n+--------------------------------------------------+");
        System.out.println("|         TRANSACTION SYSTEM - WEEK 8              |");
        System.out.println("|    Overriding + Overloading Demonstration        |");
        System.out.println("+--------------------------------------------------+");
        
        createSampleData();
        
        while (true) {
            if (!bank.isLoggedIn()) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }
    
    private static void showLoginMenu() {
        System.out.println("\n+----------------------------------------+");
        System.out.println("|              MAIN MENU                 |");
        System.out.println("+----------------------------------------+");
        System.out.println("|  1. Open New Account                   |");
        System.out.println("|  2. Login                              |");
        System.out.println("|  3. Show All Customers                 |");
        System.out.println("|  4. Show Statistics                    |");
        System.out.println("|  5. Exit                               |");
        System.out.println("+----------------------------------------+");
        System.out.print("\n[?] Choose: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1: register(); break;
            case 2: login(); break;
            case 3: bank.showAllCustomers(); break;
            case 4: bank.showStatistics(); break;
            case 5: System.out.println("\n[OK] Goodbye!"); System.exit(0); break;
            default: System.out.println("[ERROR] Invalid choice!");
        }
    }
    
    private static void showMainMenu() {
        String name = bank.getCurrentCustomer().getFullName();
        System.out.println("\n+--------------------------------------------------+");
        System.out.println("|     WELCOME, " + String.format("%-32s", name.toUpperCase()) + "|");
        System.out.println("+--------------------------------------------------+");
        System.out.println("|  1. Send Money                                 |");
        System.out.println("|  2. Request Money                              |");
        System.out.println("|  3. Check Balance                              |");
        System.out.println("|  4. Transaction History                        |");
        System.out.println("|  5. Show All Customers                         |");
        System.out.println("|  6. Pending Requests                           |");
        System.out.println("|  7. Approve Request                            |");
        System.out.println("|  8. Change Password                            |");
        System.out.println("|  9. Deposit Money (3 Ways)                     |");
        System.out.println("| 10. Withdraw Money                             |");
        System.out.println("| 11. Logout                                     |");
        System.out.println("+--------------------------------------------------+");
        System.out.print("\n[?] Choose: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1: sendMoney(); break;
            case 2: requestMoney(); break;
            case 3: bank.checkBalance(); break;
            case 4: bank.showTransactionHistory(); break;
            case 5: bank.showAllCustomers(); break;
            case 6: bank.showPendingRequests(); break;
            case 7: approveRequest(); break;
            case 8: changePassword(); break;
            case 9: depositMenu(); break;
            case 10: withdrawMenu(); break;
            case 11: bank.logout(); break;
            default: System.out.println("[ERROR] Invalid choice!");
        }
    }
    
    private static void depositMenu() {
        Customer current = bank.getCurrentCustomer();
        if (current == null) {
            System.out.println("[ERROR] Please login first!");
            return;
        }
        
        System.out.println("\n+--------------------------------------------------+");
        System.out.println("|           DEPOSIT MONEY (3 WAYS)                 |");
        System.out.println("+--------------------------------------------------+");
        System.out.println("|  1. Receive from another account (Online)        |");
        System.out.println("|  2. ATM Deposit (Cash/Check at ATM)              |");
        System.out.println("|  3. Teller Deposit (Bank Counter)                |");
        System.out.println("|  4. Back to Menu                                 |");
        System.out.println("+--------------------------------------------------+");
        System.out.print("\n[?] Choose: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                System.out.print("Enter amount: $");
                double amount = scanner.nextDouble();
                scanner.nextLine();
                System.out.print("From account number: ");
                String fromAccount = scanner.nextLine();
                System.out.print("Transaction ID: ");
                String txId = scanner.nextLine();
                System.out.print("Note: ");
                String note = scanner.nextLine();
                // FIXED: Now accurately targets clean overloaded deposit(...) version 1
                current.deposit(amount, fromAccount, txId, note);
                break;
            case 2:
                System.out.print("Enter amount: $");
                amount = scanner.nextDouble();
                scanner.nextLine();
                System.out.print("ATM ID (e.g., ATM001): ");
                String atmId = scanner.nextLine();
                System.out.print("ATM Location: ");
                String location = scanner.nextLine();
                System.out.print("Deposit Type (Cash/Check): ");
                String depositType = scanner.nextLine();
                System.out.print("Envelope ID: ");
                String envelopeId = scanner.nextLine();
                // FIXED: Now accurately targets clean overloaded deposit(...) version 2
                current.deposit(amount, atmId, location, depositType, envelopeId);
                break;
            case 3:
                System.out.print("Enter amount: $");
                amount = scanner.nextDouble();
                scanner.nextLine();
                System.out.print("Teller ID: ");
                String tellerId = scanner.nextLine();
                System.out.print("Teller Name: ");
                String tellerName = scanner.nextLine();
                System.out.print("Source Type (Cash/Check): ");
                String source = scanner.nextLine();
                System.out.print("Signature: ");
                String signature = scanner.nextLine();
                // FIXED: Now accurately targets clean overloaded deposit(...) version 3
                current.deposit(amount, tellerId, tellerName, source, signature);
                break;
            case 4:
                return;
            default:
                System.out.println("[ERROR] Invalid choice!");
        }
        System.out.println("\n[INFO] New balance: $" + current.getBalance());
    }
    
    private static void withdrawMenu() {
        Customer current = bank.getCurrentCustomer();
        if (current == null) {
            System.out.println("[ERROR] Please login first!");
            return;
        }
        
        System.out.println("\n+--------------------------------------------------+");
        System.out.println("|           WITHDRAW MONEY                         |");
        System.out.println("+--------------------------------------------------+");
        System.out.println("|  1. Basic Withdraw                               |");
        System.out.println("|  2. Withdraw with Reason                         |");
        System.out.println("|  3. Withdraw with Receipt                        |");
        System.out.println("|  4. Back to Menu                                 |");
        System.out.println("+--------------------------------------------------+");
        System.out.print("\n[?] Choose: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                System.out.print("Enter amount: $");
                double amount = scanner.nextDouble();
                scanner.nextLine();
                current.withdraw(amount);
                break;
            case 2:
                System.out.print("Enter amount: $");
                amount = scanner.nextDouble();
                scanner.nextLine();
                System.out.print("Reason: ");
                String reason = scanner.nextLine();
                // FIXED: Targets clean overloaded withdraw(...) variation 2
                current.withdraw(amount, reason);
                break;
            case 3:
                System.out.print("Enter amount: $");
                amount = scanner.nextDouble();
                scanner.nextLine();
                System.out.print("Print receipt? (yes/no): ");
                String receipt = scanner.nextLine();
                boolean printReceipt = receipt.equalsIgnoreCase("yes");
                // FIXED: Targets clean overloaded withdraw(...) variation 3
                current.withdraw(amount, printReceipt);
                break;
            case 4:
                return;
            default:
                System.out.println("[ERROR] Invalid choice!");
        }
        System.out.println("\n[INFO] New balance: $" + current.getBalance());
    }
    
    private static void register() {
        System.out.println("\n=== OPEN NEW ACCOUNT ===\n");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Full Name: ");
        String fullName = scanner.nextLine();
        System.out.print("Phone (9-12 digits): ");
        String phone = scanner.nextLine();
        System.out.print("Initial Deposit: $");
        double deposit = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Password (min 4 chars): ");
        String password = scanner.nextLine();
        
        bank.registerCustomer(username, fullName, phone, deposit, password);
    }
    
    private static void login() {
        System.out.println("\n=== LOGIN ===\n");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        bank.login(username, password);
    }
    
    private static void sendMoney() {
        System.out.println("\n=== SEND MONEY ===\n");
        bank.showAllCustomers();
        System.out.print("\nSend to (username): ");
        String to = scanner.nextLine();
        System.out.print("Amount: $");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Description (or press Enter for no description): ");
        String desc = scanner.nextLine();
        
        if (desc.isEmpty()) {
            bank.sendMoney(to, amount);
        } else {
            bank.sendMoney(to, amount, desc);
        }
        System.out.println("[INFO] New balance: $" + bank.getCurrentCustomer().getBalance());
    }
    
    private static void requestMoney() {
        System.out.println("\n=== REQUEST MONEY ===\n");
        bank.showAllCustomers();
        System.out.print("\nRequest from (username): ");
        String from = scanner.nextLine();
        System.out.print("Amount: $");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Reason (or press Enter for no reason): ");
        String reason = scanner.nextLine();
        
        if (reason.isEmpty()) {
            bank.requestMoney(from, amount);
        } else {
            bank.requestMoney(from, amount, reason);
        }
    }
    
    private static void approveRequest() {
        bank.showPendingRequests();
        System.out.print("\nEnter Request ID to approve: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        bank.approveRequest(id);
    }
    
    private static void changePassword() {
        System.out.println("\n=== CHANGE PASSWORD ===\n");
        System.out.print("Current password: ");
        String oldPwd = scanner.nextLine();
        System.out.print("New password (min 4 chars): ");
        String newPwd = scanner.nextLine();
        System.out.print("Confirm new password: ");
        String confirmPwd = scanner.nextLine();
        
        if (!newPwd.equals(confirmPwd)) {
            System.out.println("[ERROR] Passwords do not match!");
            return;
        }
        
        bank.getCurrentCustomer().changePassword(oldPwd, newPwd);
    }
    
    private static void createSampleData() {
        System.out.println("\n[INFO] Creating sample customers...");
        bank.registerCustomer("sokha", "Sokha Chea", "012345678", 1000, "1234");
        bank.registerCustomer("dara", "Dara Pich", "098765432", 500, "1234");
        bank.registerCustomer("sreymom", "Srey Mom", "011223344", 2000, "1234");
        bank.registerCustomer("vannak", "Vannak Khiev", "015566778", 750, "1234");
        System.out.println("[OK] Sample database ready!\n");

        // ===================================================================
        // POLYMORPHISM & METHOD OVERLOADING TESTS FOR LAB GRADING
        // ===================================================================
        try {
            Customer customer1 = bank.getAllCustomers().get(0); // sokha
            Customer customer2 = bank.getAllCustomers().get(1); // dara

            System.out.println("=== Test A: Class Polymorphism (Using User Type) ===");
            // Declared type of collection is abstract Parent class (User)
            java.util.ArrayList<com.transaction.model.User> users = new java.util.ArrayList<>();
            users.add(customer1); 
            users.add(customer2);

            for (com.transaction.model.User user : users) {
                // Runtime Polymorphism: Java automatically chooses correct overridden behavior
                user.displayInfo();
                System.out.println();
            }

            System.out.println("=== Test B: Interface Polymorphism (Using Displayable Interface) ===");
            // Declared type of collection is Interface
            java.util.ArrayList<com.transaction.interfaces.Displayable> displayItems = new java.util.ArrayList<>();
            displayItems.add(customer1);
            displayItems.add(customer2);

            for (com.transaction.interfaces.Displayable item : displayItems) {
                item.displayInfo();
                System.out.println();
            }

            System.out.println("=== Test C: Method Overloading (Deposits & Withdrawals Verification) ===");
            System.out.println("Executing Overloaded Deposit (Online Transfer Route):");
            customer1.deposit(200.0, "ACC9999", "TXN10293", "Monthly Allowance");

            System.out.println("\nExecuting Overloaded Deposit (ATM Terminal Route):");
            customer1.deposit(50.0, "ATM_05", "Chroy Chongvar Branch", "Cash", "ENV-882");

            System.out.println("\nExecuting Overloaded Withdrawal (Basic Option):");
            customer2.withdraw(100.0);

            System.out.println("\nExecuting Overloaded Withdrawal (With Reason Logged):");
            customer2.withdraw(500.0, "Paying tuition fees");

            System.out.println("\nExecuting Overloaded Withdrawal (With Receipt Flagged True):");
            customer2.withdraw(20.0, true);
            
            System.out.println("\n===================================================================\n");
        } catch (Exception e) {
            System.out.println("[INFO] Skipping initialization track tests: " + e.getMessage());
        }
    }
}
