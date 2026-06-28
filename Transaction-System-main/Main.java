package com.transaction;

import com.transaction.model.Customer;
import com.transaction.model.Admin;
import com.transaction.model.User;
import com.transaction.model.Transaction;
import com.transaction.model.TransactionType;
import com.transaction.model.MoneyRequest;
import com.transaction.interfaces.Displayable;
import com.transaction.service.BankService;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.InputMismatchException;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static BankService bank = new BankService();
    
    public static void main(String[] args) {
        System.out.println("\n+--------------------------------------------------+");
        System.out.println("|         TRANSACTION SYSTEM - WEEK 10            |");
        System.out.println("|         EXCEPTION HANDLING DEMONSTRATION        |");
        System.out.println("+--------------------------------------------------+");
        
        createSampleData();
        
        testClassPolymorphism();
        testInterfacePolymorphism();
        
        while (true) {
            if (!bank.isLoggedIn()) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }
    
    private static void testClassPolymorphism() {
        System.out.println("\n========== CLASS POLYMORPHISM (User Type) ==========\n");
        
        try {
            Customer sokha = new Customer("sokha", "Sokha Chea", "012345678", 1000, "1234");
            Customer dara = new Customer("dara", "Dara Pich", "098765432", 500, "1234");
            Admin admin1 = new Admin("admin1", "System Admin", "099999999", "admin123", 3);
            
            ArrayList<User> users = new ArrayList<>();
            users.add(sokha);
            users.add(dara);
            users.add(admin1);
            
            System.out.println("Looping through Users (Polymorphism in action!):");
            System.out.println("Same method call (displayInfo), different results!\n");
            
            for (User user : users) {
                System.out.println("Declared type: User | Actual type: " + user.getClass().getSimpleName());
                user.displayInfo();
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Polymorphism test failed: " + e.getMessage());
        }
    }
    
    private static void testInterfacePolymorphism() {
        System.out.println("\n========== INTERFACE POLYMORPHISM (Displayable Type) ==========\n");
        
        try {
            Customer sokha = new Customer("sokha", "Sokha Chea", "012345678", 1000, "1234");
            Admin admin1 = new Admin("admin1", "System Admin", "099999999", "admin123", 3);
            
            ArrayList<Displayable> displayItems = new ArrayList<>();
            displayItems.add(sokha);
            displayItems.add(admin1);
            
            System.out.println("Looping through Displayable items (Polymorphism in action!):");
            System.out.println("Same method call (displayInfo), different results!\n");
            
            for (Displayable item : displayItems) {
                System.out.println("Declared type: Displayable | Actual type: " + item.getClass().getSimpleName());
                item.displayInfo();
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Interface polymorphism test failed: " + e.getMessage());
        }
    }
    
    private static void showLoginMenu() {
        System.out.println("\n+----------------------------------------+");
        System.out.println("|              MAIN MENU                  |");
        System.out.println("+----------------------------------------+");
        System.out.println("|  1. Open New Account                   |");
        System.out.println("|  2. Login                              |");
        System.out.println("|  3. Show All Customers                 |");
        System.out.println("|  4. Show Statistics                    |");
        System.out.println("|  5. Test Polymorphism (Class)          |");
        System.out.println("|  6. Test Polymorphism (Interface)      |");
        System.out.println("|  7. Exit                               |");
        System.out.println("+----------------------------------------+");
        System.out.print("\n[?] Choose: ");
        
        int choice = getIntInput("Choose option: ");
        
        switch (choice) {
            case 1: register(); break;
            case 2: login(); break;
            case 3: bank.showAllCustomers(); break;
            case 4: bank.showStatistics(); break;
            case 5: testClassPolymorphism(); break;
            case 6: testInterfacePolymorphism(); break;
            case 7: System.out.println("\n[OK] Goodbye!"); System.exit(0); break;
            default: System.out.println("[ERROR] Invalid choice! Please choose 1-7.");
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
        System.out.println("|  9. Deposit Money (4 Ways)                     |");
        System.out.println("| 10. Withdraw Money (3 Ways)                    |");
        System.out.println("| 11. Test Polymorphism (Class)                  |");
        System.out.println("| 12. Test Polymorphism (Interface)              |");
        System.out.println("| 13. Logout                                     |");
        System.out.println("+--------------------------------------------------+");
        System.out.print("\n[?] Choose: ");
        
        int choice = getIntInput("Choose option: ");
        
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
            case 11: testClassPolymorphism(); break;
            case 12: testInterfacePolymorphism(); break;
            case 13: bank.logout(); break;
            default: System.out.println("[ERROR] Invalid choice! Please choose 1-13.");
        }
    }
    
    // ========== HELPER METHOD FOR SAFE INTEGER INPUT ==========
    private static int getIntInput(String prompt) {
        int input = 0;
        boolean valid = false;
        
        while (!valid) {
            try {
                input = scanner.nextInt();
                scanner.nextLine();
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("[ERROR] Invalid input! Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
                System.out.print("\n[?] " + prompt);
            }
        }
        return input;
    }
    
    // ========== HELPER METHOD FOR SAFE DOUBLE INPUT ==========
    private static double getDoubleInput(String prompt) {
        double input = 0;
        boolean valid = false;
        
        while (!valid) {
            try {
                input = scanner.nextDouble();
                scanner.nextLine();
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("[ERROR] Invalid input! Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
                System.out.print("\n[?] " + prompt);
            }
        }
        return input;
    }
    
    private static void depositMenu() {
        Customer current = bank.getCurrentCustomer();
        if (current == null) {
            System.out.println("[ERROR] Please login first!");
            return;
        }
        
        System.out.println("\n+--------------------------------------------------+");
        System.out.println("|           DEPOSIT MONEY (4 WAYS)                  |");
        System.out.println("+--------------------------------------------------+");
        System.out.println("|  1. Basic Deposit (amount only)                  |");
        System.out.println("|  2. Receive from another account (Online)        |");
        System.out.println("|  3. ATM Deposit (Cash/Check at ATM)              |");
        System.out.println("|  4. Teller Deposit (Bank Counter)                |");
        System.out.println("|  5. Back to Menu                                 |");
        System.out.println("+--------------------------------------------------+");
        System.out.print("\n[?] Choose: ");
        
        int choice = getIntInput("Choose deposit method: ");
        
        switch (choice) {
            case 1:
                try {
                    System.out.print("Enter amount: $");
                    double amount = getDoubleInput("Enter amount: $");
                    current.deposit(amount);
                } catch (IllegalArgumentException e) {
                    System.out.println("[ERROR] " + e.getMessage());
                }
                break;
            case 2:
                try {
                    System.out.print("Enter amount: $");
                    double amount = getDoubleInput("Enter amount: $");
                    System.out.print("From account number: ");
                    String fromAccount = scanner.nextLine();
                    System.out.print("Transaction ID: ");
                    String txId = scanner.nextLine();
                    System.out.print("Note: ");
                    String note = scanner.nextLine();
                    current.deposit(amount, fromAccount, txId, note);
                } catch (IllegalArgumentException e) {
                    System.out.println("[ERROR] " + e.getMessage());
                }
                break;
            case 3:
                try {
                    System.out.print("Enter amount: $");
                    double amount = getDoubleInput("Enter amount: $");
                    System.out.print("ATM ID: ");
                    String atmId = scanner.nextLine();
                    System.out.print("ATM Location: ");
                    String location = scanner.nextLine();
                    System.out.print("Deposit Type: ");
                    String depositType = scanner.nextLine();
                    System.out.print("Envelope ID: ");
                    String envelopeId = scanner.nextLine();
                    current.depositATM(amount, atmId, location, depositType, envelopeId);
                } catch (IllegalArgumentException e) {
                    System.out.println("[ERROR] " + e.getMessage());
                }
                break;
            case 4:
                try {
                    System.out.print("Enter amount: $");
                    double amount = getDoubleInput("Enter amount: $");
                    System.out.print("Teller ID: ");
                    String tellerId = scanner.nextLine();
                    System.out.print("Teller Name: ");
                    String tellerName = scanner.nextLine();
                    System.out.print("Source Type: ");
                    String source = scanner.nextLine();
                    System.out.print("Signature: ");
                    String signature = scanner.nextLine();
                    current.depositTeller(amount, tellerId, tellerName, source, signature);
                } catch (IllegalArgumentException e) {
                    System.out.println("[ERROR] " + e.getMessage());
                }
                break;
            case 5:
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
        System.out.println("|           WITHDRAW MONEY (3 WAYS)                 |");
        System.out.println("+--------------------------------------------------+");
        System.out.println("|  1. Basic Withdraw                               |");
        System.out.println("|  2. Withdraw with Reason                         |");
        System.out.println("|  3. Withdraw with Receipt                        |");
        System.out.println("|  4. Back to Menu                                 |");
        System.out.println("+--------------------------------------------------+");
        System.out.print("\n[?] Choose: ");
        
        int choice = getIntInput("Choose withdraw option: ");
        
        switch (choice) {
            case 1:
                try {
                    System.out.print("Enter amount: $");
                    double amount = getDoubleInput("Enter amount: $");
                    current.withdraw(amount);
                } catch (IllegalArgumentException e) {
                    System.out.println("[ERROR] " + e.getMessage());
                }
                break;
            case 2:
                try {
                    System.out.print("Enter amount: $");
                    double amount = getDoubleInput("Enter amount: $");
                    System.out.print("Reason: ");
                    String reason = scanner.nextLine();
                    current.withdrawReason(amount, reason);
                } catch (IllegalArgumentException e) {
                    System.out.println("[ERROR] " + e.getMessage());
                }
                break;
            case 3:
                try {
                    System.out.print("Enter amount: $");
                    double amount = getDoubleInput("Enter amount: $");
                    System.out.print("Print receipt? (yes/no): ");
                    String receipt = scanner.nextLine();
                    boolean printReceipt = receipt.equalsIgnoreCase("yes");
                    current.withdrawReceipt(amount, printReceipt);
                } catch (IllegalArgumentException e) {
                    System.out.println("[ERROR] " + e.getMessage());
                }
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
        
        try {
            System.out.print("Username: ");
            String username = scanner.nextLine();
            
            System.out.print("Full Name: ");
            String fullName = scanner.nextLine();
            
            System.out.print("Phone (9-12 digits): ");
            String phone = scanner.nextLine();
            
            System.out.print("Initial Deposit: $");
            double deposit = getDoubleInput("Initial Deposit: $");
            
            System.out.print("Password (min 4 chars): ");
            String password = scanner.nextLine();
            
            bank.registerCustomer(username, fullName, phone, deposit, password);
        } catch (Exception e) {
            System.out.println("[ERROR] Registration failed: " + e.getMessage());
        }
    }
    
    private static void login() {
        System.out.println("\n=== LOGIN ===\n");
        
        try {
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            
            bank.login(username, password);
        } catch (Exception e) {
            System.out.println("[ERROR] Login failed: " + e.getMessage());
        }
    }
    
    private static void sendMoney() {
        System.out.println("\n=== SEND MONEY ===\n");
        bank.showAllCustomers();
        
        try {
            System.out.print("\nSend to (username): ");
            String to = scanner.nextLine();
            System.out.print("Amount: $");
            double amount = getDoubleInput("Enter amount: $");
            System.out.print("Description: ");
            String desc = scanner.nextLine();
            
            if (desc.isEmpty()) {
                bank.sendMoney(to, amount);
            } else {
                bank.sendMoney(to, amount, desc);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to send money: " + e.getMessage());
        }
    }
    
    private static void requestMoney() {
        System.out.println("\n=== REQUEST MONEY ===\n");
        bank.showAllCustomers();
        
        try {
            System.out.print("\nRequest from (username): ");
            String from = scanner.nextLine();
            System.out.print("Amount: $");
            double amount = getDoubleInput("Enter amount: $");
            System.out.print("Reason: ");
            String reason = scanner.nextLine();
            
            if (reason.isEmpty()) {
                bank.requestMoney(from, amount);
            } else {
                bank.requestMoney(from, amount, reason);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to request money: " + e.getMessage());
        }
    }
    
    private static void approveRequest() {
        try {
            bank.showPendingRequests();
            System.out.print("\nEnter Request ID to approve: ");
            int id = getIntInput("Enter Request ID to approve: ");
            bank.approveRequest(id);
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to approve request: " + e.getMessage());
        }
    }
    
    private static void changePassword() {
        System.out.println("\n=== CHANGE PASSWORD ===\n");
        
        try {
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
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to change password: " + e.getMessage());
        }
    }
    
    private static void createSampleData() {
        System.out.println("\n[INFO] Creating sample customers...");
        try {
            bank.registerCustomer("sokha", "Sokha Chea", "012345678", 1000, "1234");
            bank.registerCustomer("dara", "Dara Pich", "098765432", 500, "1234");
            bank.registerCustomer("sreymom", "Srey Mom", "011223344", 2000, "1234");
            bank.registerCustomer("vannak", "Vannak Khiev", "015566778", 750, "1234");
            System.out.println("[OK] Sample database ready!\n");
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to create sample data: " + e.getMessage());
        }
    }
}