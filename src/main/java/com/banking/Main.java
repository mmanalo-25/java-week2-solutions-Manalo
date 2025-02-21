package com.banking;


import com.banking.model.*;
import com.banking.service.AccountService;
import com.banking.exception.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * A simple banking application that shows basic account operations.
 * This program demonstrates:
 * 1. How to create bank accounts
 * 2. How to deposit money
 * 3. How to withdraw money
 * 4. How to transfer between accounts
 * 5. How to handle errors
 */

public class Main {
    static BankingSystem bankingSystem = new BankingSystem();
    static AccountService accountService = new AccountService(bankingSystem);
    static Scanner input = new Scanner(System.in);
    static BigDecimal amount = BigDecimal.ZERO;
    static String checkingAccountNumber = "";
    static String savingsaccountNumber = "";
    static boolean isTrue = false;
    static String accountNumber;

    public static void main(String[] args) {
        // Create objects we need
        try {
            do {

                mainDashboard();
            } while (true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }





/*
        try {

            // Now let's see what happens when we make mistakes
            System.out.println("\nTrying some operations that should fail:");




        } catch (BankingException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/


    }

    public static void mainDashboard() throws SQLException {

        System.out.println("=== Banking System Menu ===\n" +
                "1. Create Account\n" +
                "2. View Account\n" +
                "3. Deposit\n" +
                "4. Withdraw\n" +
                "5. Transfer\n" +
                "6. View Transactions\n" +
                "7. Process Monthly Fee\n" +
                "8. Exit\n");
        System.out.print("Enter your choice: ");

        switch (input.nextLine()) {
            case "1":
                createAccount();
                break;
            case "2":
                viewAccount();
                break;
            case "3":
                deposit();
                break;
            case "4":
                withdraw();
                break;
            case "5":
                transfer();
                break;
            case "6":
                System.out.println("View Transactions");
                System.out.println("1. Get all transactions order by Account Number and Transaction  ");
                System.out.println("2. Get transactions for specific account:");
                System.out.print("Enter you choice : ");
                switch (input.nextLine()) {
                    case "1":
                        System.out.println("\nGet all transactions order by Account Number and Transaction");
                        accountService.getTransactionOrderByAccountNumber();

                        break;
                    case "2":
                        System.out.println("\nGet transactions for specific account:");
                        try {
                            System.out.print("Enter account number: ");
                            accountNumber = input.nextLine();
                            accountService.findAccount(accountNumber);

                            accountService.getTransactionByAccountNumber(accountNumber);
                        } catch (AccountNotFoundException e) {
                            System.out.println("Failed: This account number doesn't exist");
                        }

                        break;
                    default:
                        System.out.println("Invalid input. Choose between 1 or 2 only");
                        break;


                }

//                System.out.println("\nGet accounts sorted by balance");
//                accountService.getAccountsSortedByBalance();

                mainDashboard();

                break;
            case "7":
                System.out.println("\nProcess monthly fees:");
                accountService.processMonthlyFees();
                System.out.println("Successfully processed monthly fee for all accounts.");
                mainDashboard();
                break;
            case "8":

                do {
                    System.out.print("Do you want to exit? ");
                    String choice = input.nextLine();

                    if (choice.equalsIgnoreCase("y")) {
                        System.exit(0);

                    } else if (choice.equalsIgnoreCase("n")) {
                        mainDashboard();
                        isTrue = true;
                    } else {
                        System.out.println("Invalid input. Choose between y or n only ");

                    }

                } while (!isTrue);
                break;

            default:
                System.out.println("Invalid Input. Select between 1-8 only.");
                break;
        }
    }

    public static void createAccount() {
        System.out.println("Create Account:");
        System.out.println("1. Savings");
        System.out.println("2. Checking");
        System.out.println("3. Back");
        System.out.print("Enter choice: ");
        try {
            switch (input.nextLine()) {
                case "1":
                    System.out.println("Create Savings Account: ");
                    System.out.print("Enter account number: " + AccountType.SAVINGS.getCode());
                    savingsaccountNumber = AccountType.SAVINGS.getCode() + input.nextLine();
                    try {
                        System.out.print("Enter initial deposit: ");
                        amount = input.nextBigDecimal();
                        // Create a savings account with $1000
                        Account savings = accountService.createAccount(accountService.getNextAccountID(),
                                AccountType.SAVINGS,
                                savingsaccountNumber,
                                amount
                        );
                        System.out.println("Created savings account " + savingsaccountNumber + " with initial deposit $" + amount + ".");

                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                    } catch (BankingException e) {
                        System.out.println("Failed: Can't deposit negative money");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case "2":
                    System.out.println("Create Checking Account: ");
                    System.out.print("Enter account number: " + AccountType.CHECKING.getCode());
                    checkingAccountNumber = AccountType.CHECKING.getCode() + input.nextLine();
                    try {
                        System.out.print("Enter initial deposit: ");
                        amount = input.nextBigDecimal();
                        Account checking = accountService.createAccount(accountService.getNextAccountID(),
                                AccountType.CHECKING,
                                checkingAccountNumber,
                                amount
                        );

                        System.out.println("Created checking account " + checkingAccountNumber + " with initial deposit " + amount + ".");

                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                        input.nextLine();

                    } catch (BankingException e) {
                        System.out.println("Failed: Can't deposit negative money");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "3":
                    mainDashboard();
                    break;
                default:
                    System.out.println("Invalid input. Choose between 1 or 2 only ");
                    break;

            }
            input.nextLine();

            do {
                System.out.print("Do you want to create another account? (y/n):");
                String choice = input.nextLine();

                if (choice.equalsIgnoreCase("y")) {
                    createAccount();
                    isTrue = true;
                } else if (choice.equalsIgnoreCase("n")) {
                    mainDashboard();
                    isTrue = true;
                } else {
                    System.out.println("Invalid input. Choose between y or n only ");

                }

            } while (!isTrue);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void viewAccount() {
        try {
            System.out.println("\nView Account:");
            try {
                System.out.print("Enter account number: ");
                accountNumber = input.nextLine();
                Account account = accountService.findAccount(accountNumber);
                System.out.printf("| %-15s | %-13s | %-13s | \n",
                        "ACCOUNT NUMBER", "ACCOUNT TYPE", "BALANCE");
                System.out.printf("| %-15s | %-13s | $ %-13s |\n",
                        account.getAccountNumber(), account.getAccountType(), account.getBalance());
            } catch (AccountNotFoundException e) {
                System.out.println("Failed: This account number doesn't exist");
            }


            do {
                System.out.print("Do you want to view account? (y/n):");
                String choice = input.nextLine();

                if (choice.equalsIgnoreCase("y")) {
                    viewAccount();
                    isTrue = true;
                } else if (choice.equalsIgnoreCase("n")) {
                    mainDashboard();
                    isTrue = true;
                } else {
                    System.out.println("Invalid input. Choose between y or n only ");

                }

            } while (!isTrue);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public static void deposit() {
        System.out.println("\nDeposit:");
        try {
            try {
                System.out.print("Enter account number: ");
                accountNumber = input.nextLine();
                accountService.findAccount(accountNumber);
                do {
                    try {

                        System.out.print("Enter deposit amount: ");
                        amount = input.nextBigDecimal();
                        accountService.deposit(accountNumber, amount);
                        System.out.println("Balance is now: $" + accountService.getBalance(accountNumber));

                    } catch (InputMismatchException e) {
                        System.out.println("Error: Invalid input. Please enter a valid deposit amount.");
                        isTrue = false;
                    } catch (BankingException e) {
                        System.out.println("Failed: Can't deposit negative money");
                    }
                } while (isTrue);

            } catch (AccountNotFoundException e) {
                System.out.println("Failed: This account number doesn't exist");

            }
            input.nextLine();
            do {
                System.out.print("Do you want to deposit again? (y/n):");
                String choice = input.nextLine();

                if (choice.equalsIgnoreCase("y")) {
                    deposit();
                    isTrue = true;
                } else if (choice.equalsIgnoreCase("n")) {
                    mainDashboard();
                    isTrue = true;
                } else {
                    System.out.println("Invalid input. Choose between y or n only ");

                }

            } while (!isTrue);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void withdraw() {
        System.out.println("\nWithdraw");
        try {
            try {
                System.out.print("Enter account number: ");
                accountNumber = input.nextLine();
                accountService.findAccount(accountNumber);
                do {
                    try {

                        System.out.print("Enter withdrawal amount: ");
                        amount = input.nextBigDecimal();
                        accountService.withdraw(accountNumber, amount);
                        System.out.println("Balance is now: $" + accountService.getBalance(accountNumber));

                    } catch (InputMismatchException e) {
                        System.out.println("Error: Invalid input. Please enter a valid withdrawal amount.");
                        isTrue = false;
                    } catch (InsufficientFundsException e) {
                        System.out.println("Failed: Not enough money in account");
                    } catch (BankingException e) {
                        System.out.println("Failed: Can't withdraw negative money");
                    }
                } while (isTrue);

            } catch (AccountNotFoundException e) {
                System.out.println("Error: The account number entered does not exist.");
            } catch (InputMismatchException e) {
                System.out.println("Error: Invalid input. Please enter a valid deposit amount.");
            }
            input.nextLine();
            do {
                System.out.print("Do you want to withdraw again? (y/n):");
                String choice = input.nextLine();

                if (choice.equalsIgnoreCase("y")) {
                    withdraw();
                    isTrue = true;
                } else if (choice.equalsIgnoreCase("n")) {
                    mainDashboard();
                    isTrue = true;
                } else {
                    System.out.println("Invalid input. Choose between y or n only ");

                }

            } while (!isTrue);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void transfer() {
        System.out.println("\nTransfer");
        try {
            try {
                System.out.print("FROM: Enter account number: ");
                String fromAccountNumber = input.nextLine();
                accountService.findAccount(fromAccountNumber);

                System.out.print("TO: Enter account number: ");
                String toAccountNumber = input.nextLine();
                accountService.findAccount(toAccountNumber);

                System.out.print("Enter transfer amount: ");
                amount = input.nextBigDecimal();

                accountService.transfer(fromAccountNumber, toAccountNumber, amount);
                System.out.println("After transfer:");
                System.out.println("FROM: Account Number " + fromAccountNumber + " New Balance: $" + accountService.getBalance(fromAccountNumber));
                System.out.println("TO: Account Number  " + toAccountNumber + " New Balance: $" + accountService.getBalance(toAccountNumber));

            } catch (AccountNotFoundException e) {
                System.out.println("Error: The account number entered does not exist.");
            } catch (InputMismatchException e) {
                System.out.println("Error: Invalid input. Please enter a valid deposit amount.");
            } catch (InsufficientFundsException e) {
                System.out.println("Error: Insufficient funds.");
            } catch (BankingException e) {
                System.out.println("Failed: Can't transfer negative money");
            }
            input.nextLine();
            do {
                System.out.print("Do you want to transfer again? (y/n):");
                String choice = input.nextLine();

                if (choice.equalsIgnoreCase("y")) {
                    transfer();
                    isTrue = true;
                } else if (choice.equalsIgnoreCase("n")) {
                    mainDashboard();
                    isTrue = true;
                } else {
                    System.out.println("Invalid input. Choose between y or n only ");

                }

            } while (!isTrue);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
