package com.banking.exception;

public class AccountAlreadyExist extends BankingException {
  private final String accountNumber;



    public AccountAlreadyExist(String accountNumber) {
      super(String.format("Account number %s already exist ", accountNumber));
      this.accountNumber = accountNumber;
    }

    public AccountAlreadyExist(String accountNumber, Throwable cause) {
      super(String.format("Account number %s already exist ",  accountNumber), cause);
      this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
      return accountNumber;
    }


}
