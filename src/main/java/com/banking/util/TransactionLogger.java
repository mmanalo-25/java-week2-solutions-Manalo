package com.banking.util;

import com.banking.db.AccountDAO;
import com.banking.db.AccountDAOImpl;
import java.io.*;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple transaction logger that keeps track of bank transactions.
 * Each transaction is saved in the format:
 * timestamp,accountNumber,amount
 */
public class TransactionLogger {
    // The file where we save transactions
    private static final String TRANSACTIONS_FILE = "transactions.txt";

    /**
     * Save a transaction to our log file
     */
    public void saveTransaction(int accountID, String accountNumber, BigDecimal amount, BigDecimal balance) throws SQLException {
        AccountDAO accountDAO = new AccountDAOImpl();
        accountDAO.saveTransaction(accountID, accountNumber, amount, balance);

    }

    public void getTransactionByAccountNumber(String accountNumber) {
        AccountDAO accountDAO = new AccountDAOImpl();
        accountDAO.getTransactionByAccountNumber(accountNumber);
    }
    public void getTransactionOrderByAccountNumber() {
        AccountDAO accountDAO = new AccountDAOImpl();
        accountDAO.getTransactionOrderByAccountNumber();
    }

//    *
//     * Get all transactions for a specific account
//
    public List<String> getTransactionsForAccount(String accountNumber) {
        List<String> accountTransactions = new ArrayList<>();

        try {
            // Try to read the file
            File file = new File(TRANSACTIONS_FILE);
            if (!file.exists()) {
                return accountTransactions;  // Return empty list if no file
            }

            // Read the file line by line
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            // Look for transactions for this account
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[1].equals(accountNumber)) {
                    // Format: Time: [timestamp], Account: [number], Amount: $[amount]
                    accountTransactions.add(String.format("Time: %s, Account: %s, Amount: $%s",
                        parts[0], parts[1], parts[2]));
                }
            }

            reader.close();

        } catch (IOException e) {
            System.out.println("Couldn't read transactions: " + e.getMessage());
        }

        return accountTransactions;
    }
//
//    *
//     * Get all transactions in the system
//
    public List<String> getAllTransactions() {
        List<String> allTransactions = new ArrayList<>();

        try {
            // Try to read the file
            File file = new File(TRANSACTIONS_FILE);
            if (!file.exists()) {
                return allTransactions;  // Return empty list if no file
            }

            // Read the file line by line
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    // Format: Time: [timestamp], Account: [number], Amount: $[amount]
                    allTransactions.add(String.format("Time: %s, Account: %s, Amount: $%s",
                        parts[0], parts[1], parts[2]));
                }
            }

            reader.close();

        } catch (IOException e) {
            System.out.println("Couldn't read transactions: " + e.getMessage());
        }

        return allTransactions;
    }

//    *
//     * Clear all saved transactions
//
//    public void clearTransactions() {
//        File file = new File(TRANSACTIONS_FILE);
//        if (file.exists()) {
//            file.delete();
//        }
//    }
}
