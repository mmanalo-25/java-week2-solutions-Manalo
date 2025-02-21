package com.banking;

import com.banking.db.AccountDAO;
import com.banking.db.AccountDAOImpl;
import com.banking.model.Account;
import com.banking.exception.AccountNotFoundException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class BankingSystem {
    // Generic collections
//    private final List<Account> accounts = new ArrayList<>();
//    private final Map<String, Account> accountMap = new HashMap<>();

    // Add account to both collections
    public void addAccount(Account account) throws SQLException {
        Objects.requireNonNull(account, "Account cannot be null");
        AccountDAO accountDAO = new AccountDAOImpl();
        accountDAO.addAccount(account);

     //   accounts.add(account);
      //  accountMap.put(account.getAccountNumber(), account);
    }

    // Find account using Map for O(1) lookup
    public Account findAccount(String accountNumber) throws SQLException {
//        Account account = accountMap.get(accountNumber);
        AccountDAO accountDAO = new AccountDAOImpl();
        Account account = accountDAO.findAccount(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException(accountNumber);
        }
        return account;
    }

    // Get accounts sorted by balance
    public void getAccountsSortedByBalance() {
        AccountDAO accountDAO = new AccountDAOImpl();
        if (!accountDAO.getAccountSortedByBalance())
        {
            throw new RuntimeException ("No record found.");
        }

    }

    // Process monthly fees for all accounts
    public void processMonthlyFees() {
        AccountDAO accountDAO = new AccountDAOImpl();
        List<Account> accounts = accountDAO.listOfAccounts();
        for (Account account : accounts) {
             account.processMonthlyFees();
        }


    }

    public int getNextAccountID() throws SQLException {
        AccountDAO accountDAO = new AccountDAOImpl();
        return accountDAO.getNextAccountId();
    }

//
//    // Get total balance across all accounts
//    public BigDecimal getTotalBalance() {
//        return accounts.stream()
//                      .map(Account::getBalance)
//                      .reduce(BigDecimal.ZERO, BigDecimal::add);
//    }

//    // Get accounts filtered by minimum balance
//    public List<Account> getAccountsAboveBalance(BigDecimal minimumBalance) {
//        return accounts.stream()
//                      .filter(a -> a.getBalance().compareTo(minimumBalance) > 0)
//                      .collect(Collectors.toList());
//    }
//
//    // Get number of accounts
//    public int getNumberOfAccounts() {
//        return accounts.size();
//    }
//
//    // Clear all accounts
//    public void clearAccounts() {
//        accounts.clear();
////        accountMap.clear();
//    }
}
