
package com.banking.db;

import com.banking.model.Account;
import com.banking.model.AccountFactory;
import com.banking.model.AccountType;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAOImpl implements AccountDAO, QueryConstant {

    // Use the new DBConnection class
    private final DBConnection dbConnection = new DBConnection();

    @Override
    public void addAccount(Account account)  {


        try (Connection connection = dbConnection.connect();
             PreparedStatement prep = connection.prepareStatement(INSERT_ACCOUNT)) {

            connection.setAutoCommit(false);
            prep.setString(1, account.getAccountNumber());
            prep.setString(2, account.getAccountType().toString());
            prep.setBigDecimal(3, account.getBalance());

            int rowsAffected = prep.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Account created successfully!");
                connection.commit();
            } else {
                System.out.println("Account insertion failed.");
                connection.rollback();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error adding account", e);
        }
    }

    @Override
    public Account findAccount(String accountNumber)  {

        try (Connection connection = dbConnection.connect();
             PreparedStatement prep = connection.prepareStatement(SELECT_ACCOUNTNUMBER)) {

            prep.setString(1, accountNumber);
            ResultSet resultSet = prep.executeQuery();

            if (resultSet.next()) {
                int accountID = resultSet.getInt("account_id");
                accountNumber = resultSet.getString("account_number");
                AccountType accounttype = AccountType.valueOf(resultSet.getString("account_type"));
                BigDecimal balance = resultSet.getBigDecimal("balance");
                return AccountFactory.createAccount(accountID, accounttype, accountNumber, balance);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error finding account", e);
        }
        return null;
    }

    @Override
    public void updateDeposit(String accountNumber, BigDecimal balance) {

        try (Connection connection = dbConnection.connect();
             PreparedStatement prep = connection.prepareStatement(UPDATE_DEPOSIT)) {

            prep.setBigDecimal(1, balance);
            prep.setString(2, accountNumber);
            prep.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error updating deposit", e);
        }
    }

    @Override
    public boolean getAccountSortedByBalance() {
        System.out.printf("| %-15s | %-13s | %-13s | %-13s |\n", "ACCOUNT ID", "ACCOUNT NUMBER", "ACCOUNT TYPE", "BALANCE");

        try (Connection connection = dbConnection.connect();
             Statement state = connection.createStatement();
             ResultSet result = state.executeQuery(SELECT_ALL_BYBALANCE)) {

            while (result.next()) {
                int accountID = result.getInt("account_id");
                String accountNumber = result.getString("account_number");
                String accountType = result.getString("account_type");
                BigDecimal balance = result.getBigDecimal("balance");
                System.out.printf("| %-15s | %-13s  | %-13s | $ %-13s |\n", accountID, accountNumber, accountType, balance);
            }
            return true;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error retrieving accounts", e);
        }
    }

    @Override
    public List<Account> listOfAccounts() {
        List<Account> accounts = new ArrayList<>();

        try (Connection connection = dbConnection.connect();
             Statement stmt = connection.createStatement();
             ResultSet result = stmt.executeQuery(SELECT_ALL)) {

            while (result.next()) {
                int accountID = result.getInt("account_id");
                String accountNumber = result.getString("account_number");
                AccountType accountType = AccountType.valueOf(result.getString("account_type"));
                BigDecimal balance = result.getBigDecimal("balance");

                Account account = AccountFactory.createAccount(accountID, accountType, accountNumber, balance);
                accounts.add(account);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error retrieving account list", e);
        }
        return accounts;
    }

    public void saveTransaction(int accountID, String accountNumber, BigDecimal amount, BigDecimal balance) {


        try (Connection connection = dbConnection.connect();
             PreparedStatement prep = connection.prepareStatement(INSERT_TRANSACTION)) {

            prep.setInt(1, accountID);
            prep.setBigDecimal(2, amount);
            prep.setBigDecimal(3, balance);
            prep.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error saving transaction", e);
        }
    }

    public void getTransactionByAccountNumber(String accountNumber)  {

        try (Connection connection = dbConnection.connect();
             PreparedStatement prep = connection.prepareStatement(TRANSACTIONS_BYACCOUNT)) {

            prep.setString(1, accountNumber);
            ResultSet result = prep.executeQuery();

            System.out.printf("| %-15s | %-13s | %-13s | %-13s |\n",
                    "TRANSACTION DATE", "ACCOUNT NUMBER", "AMOUNT", "BALANCE");

            while (result.next()) {
                Date trans_date = result.getDate("transaction_date");
                BigDecimal amount = result.getBigDecimal("amount");
                BigDecimal balance = result.getBigDecimal("balance");

                System.out.printf("| %-15s | %-13s | $ %-13s | $ %-13s |\n",
                        trans_date, accountNumber, amount, balance);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error retrieving transactions", e);
        }
    }

    public void getTransactionOrderByAccountNumber()  {


        try (Connection connection = dbConnection.connect();
             Statement state = connection.createStatement();
             ResultSet result = state.executeQuery(TRANSACTIONS_ORDERBYACCOUNT)) {

            System.out.printf("| %-15s | %-13s | %-13s | %-13s | %-13s |\n",
                    "TRANSACTION DATE", "ACCOUNT NUMBER", "ACCOUNT TYPE", "AMOUNT", "BALANCE");

            while (result.next()) {
                Date trans_date = result.getDate("transaction_date");
                String accountNumber = result.getString("account_number");
                String accountType = result.getString("account_type");
                BigDecimal amount = result.getBigDecimal("amount");
                BigDecimal balance = result.getBigDecimal("transaction_balance");

                System.out.printf("| %-15s | %-13s | %-13s | $ %-13s | $ %-13s |\n",
                        trans_date, accountNumber, accountType, amount, balance);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error retrieving ordered transactions", e);
        }
    }

    public int getNextAccountId() {

        try (Connection connection = dbConnection.connect();
             PreparedStatement stmt = connection.prepareStatement(SELECT_LAST_ACCOUNTID);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1) == 0 ? 1 : rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error getting next account ID", e);
        }
        return 1;
    }
}


