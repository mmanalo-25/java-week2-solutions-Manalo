package com.banking.db;

import com.banking.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDAO {
    Account findAccount(String accountNumber) ;

    void addAccount(Account account) ;

    void updateDeposit(String accountNumber, BigDecimal balance);

    boolean getAccountSortedByBalance();

    List<Account> listOfAccounts();

    void saveTransaction(int accountID, String accountNumber, BigDecimal amount, BigDecimal balance);

    void getTransactionByAccountNumber(String accountNumber) ;

    int getNextAccountId() ;

    void getTransactionOrderByAccountNumber() ;
}
