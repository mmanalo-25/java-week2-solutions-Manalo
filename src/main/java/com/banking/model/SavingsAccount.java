package com.banking.model;

import java.math.BigDecimal;

public class SavingsAccount extends Account {
    private final BigDecimal interestRate;

    public SavingsAccount(int accountID, String accountNumber, AccountType accountType, BigDecimal balance) {
        super(accountID, accountNumber, accountType, balance);
        this.interestRate = new BigDecimal("0.025"); // 2.5% interest rate
    }

    public SavingsAccount(int accountID,String accountNumber, AccountType accountType, BigDecimal balance, BigDecimal interestRate) {
        super(accountID,accountNumber, accountType, balance);
        this.interestRate = interestRate;
    }



    @Override
    public void processMonthlyFees() {
        // Calculate and add interest
        BigDecimal interest = getBalance().multiply(interestRate);
        deposit(interest);
    }

    @Override
    protected boolean canWithdraw(BigDecimal amount) {
        // Ensure minimum balance is maintained
        return getBalance().subtract(amount)
                         .compareTo(getMinimumBalance()) >= 0;
    }
//
//    public BigDecimal getInterestRate() {
//        return interestRate;
//    }

    @Override
    public String toString() {
        return String.format("SavingsAccount[number=%s, balance=%.2f, interestRate=%.2f%%]",
                           getAccountNumber(),
                           getBalance(),
                           interestRate.multiply(new BigDecimal("100")));
    }
}
