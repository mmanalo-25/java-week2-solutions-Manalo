package com.banking.db;

public interface QueryConstant {
    String INSERT_ACCOUNT = "INSERT INTO accounts (account_number, account_type, balance) VALUES (?, ?, ?)";
    String SELECT_ACCOUNTNUMBER = "Select * from accounts where account_number = ?";
    String UPDATE_DEPOSIT= "UPDATE accounts SET balance = ? WHERE account_number = ?";
    String SELECT_ALL_BYBALANCE = "Select * from accounts ORDER BY balance ";
    String SELECT_ALL = "SELECT * FROM accounts";

    String INSERT_TRANSACTION = "INSERT INTO transactions ( account_id, transaction_date, amount, balance) " +
            "VALUES (?, DEFAULT, ?,  ?)";
    String TRANSACTIONS_BYACCOUNT = "SELECT t.transaction_date, a.account_number, t.amount, t.balance " +
            "FROM transactions t " +
            "INNER JOIN accounts a ON t.account_id = a.account_id " +
            "WHERE a.account_number = ? " +
            "ORDER BY t.transaction_date ASC";
    String TRANSACTIONS_ORDERBYACCOUNT = "SELECT a.account_id, " +
            "a.account_number, " +
            "a.account_type, " +
            "a.balance AS account_balance, " +
            "t.transaction_ID, " +
            "t.transaction_date, " +
            "t.amount, " +
            "t.balance AS transaction_balance " +
            "FROM transactions t " +
            "INNER JOIN accounts a ON t.account_id = a.account_id " +
            "ORDER BY a.account_number ASC, t.transaction_ID ASC;";

    String SELECT_LAST_ACCOUNTID = "SELECT MAX(account_id) + 1 FROM accounts";

}
