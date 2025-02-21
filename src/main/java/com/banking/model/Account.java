package com.banking.model;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.banking.db.AccountDAO;
import com.banking.db.AccountDAOImpl;
import com.banking.exception.InsufficientFundsException;
import com.banking.util.TransactionLogger;

public abstract class Account {
    // Private fields - data encapsulation
    private final int accountID;
    private final String accountNumber;
    private BigDecimal balance;
    private final AccountType accountType;
    // Protected field - accessible by subclasses


    // Static field - shared across instances
    private static final BigDecimal MINIMUM_BALANCE = new BigDecimal("100.00");
    private final TransactionLogger logger = new TransactionLogger();
    private final   AccountDAO accountDAO = new AccountDAOImpl();


    // Public constructor
    public Account(int accountID, String accountNumber, AccountType accountType, BigDecimal balance) {
        this.accountID = accountID;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;

    }

    //Setters
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    // Getters
    public AccountType getAccountType() {
        return accountType;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public BigDecimal getBalance() { return balance; }

    // Protected method for subclasses
    protected abstract boolean canWithdraw(BigDecimal amount);
    protected BigDecimal getMinimumBalance() {
        return MINIMUM_BALANCE;
    }

    // Abstract method
    public abstract void processMonthlyFees();

    // Concrete method with virtual invocation
    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        if (!canWithdraw(amount)) {
            throw new InsufficientFundsException(accountNumber, amount, balance);
        }

        balance = balance.subtract(amount);
        accountDAO.updateDeposit(accountNumber, balance);
        try{
            logger.saveTransaction(accountID,accountNumber, amount.negate(), getBalance());
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        balance = balance.add(amount);
        accountDAO.updateDeposit(accountNumber, balance);
        try{
            logger.saveTransaction(accountID, accountNumber, amount, getBalance());
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Override Object class methods
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Account)) return false;
        Account other = (Account) obj;
        return accountNumber.equals(other.accountNumber);
    }

    @Override
    public int hashCode() {
        return accountNumber.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Account[number=%s, balance=%.2f]",
                           accountNumber, balance);
    }
}
