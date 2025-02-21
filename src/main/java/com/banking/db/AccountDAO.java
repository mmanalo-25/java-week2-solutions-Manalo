package com.banking.db;

import com.banking.model.Account;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface AccountDAO {
    public Account findAccount(String accountNumber) throws SQLException ;
    public void addAccount(Account account) throws SQLException;
    public void updateDeposit(String accountNumber, BigDecimal balance);
    public boolean getAccountSortedByBalance();
    public List<Account> listOfAccounts();
    public void saveTransaction(int accountID, String accountNumber, BigDecimal amount, BigDecimal balance) throws SQLException;
    public void getTransactionByAccountNumber(String accountNumber) throws SQLException;
    public int getNextAccountId() throws SQLException ;
    public void getTransactionOrderByAccountNumber()throws SQLException;
}
