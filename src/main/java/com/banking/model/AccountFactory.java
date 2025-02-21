package com.banking.model;

import java.math.BigDecimal;

public class AccountFactory {
    public static Account createAccount(int accountID, AccountType type, String accountNumber, BigDecimal balance) {
        switch (type) {
            case SAVINGS:
                return new SavingsAccount(accountID, accountNumber, type, balance);
            case CHECKING:
                return new CheckingAccount(accountID, accountNumber, type, balance);
            default:
                throw new IllegalArgumentException("Unknown account type: " + type);
        }
    }


}
