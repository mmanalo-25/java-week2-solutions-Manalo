package com.banking.service;

import com.banking.BankingSystem;
import com.banking.model.*;
import com.banking.exception.*;
import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import static org.junit.Assert.*;

public class AccountServiceTest {
    private BankingSystem bankingSystem;
    private AccountService accountService;
    private String savingsAccountId = "SAV001";
    private String checkingAccountId = "CHK001";

    @Before
    public void setUp() throws BankingException, SQLException {
        bankingSystem = new BankingSystem();
        accountService = new AccountService(bankingSystem);

        // Create test accounts using AccountService
        accountService.createAccount(-1,
            AccountType.SAVINGS,
            savingsAccountId,
            new BigDecimal("1000.00")
        );

        accountService.createAccount(-1,
            AccountType.CHECKING,
            checkingAccountId,
            new BigDecimal("500.00")
        );
    }

    @Test
    public void testCreateAccount() throws BankingException, SQLException {
        // Arrange
        String newAccountId = "SAV002";
        BigDecimal initialBalance = new BigDecimal("1500.00");

        // Act
        Account account = accountService.createAccount(-1,
            AccountType.SAVINGS,
            newAccountId,
            initialBalance
        );

        // Assert
        assertEquals(newAccountId, account.getAccountNumber());
        assertEquals(initialBalance, accountService.getBalance(newAccountId));
    }

    @Test
    public void testDeposit() throws BankingException, SQLException {
        // Arrange
        BigDecimal depositAmount = new BigDecimal("500.00");
        BigDecimal expectedBalance = new BigDecimal("1500.00");

        // Act
        accountService.deposit(savingsAccountId, depositAmount);

        // Assert
        assertEquals(expectedBalance, accountService.getBalance(savingsAccountId));
    }

    @Test(expected = BankingException.class)
    public void testDepositNegativeAmount() throws BankingException, SQLException {
        accountService.deposit(savingsAccountId, new BigDecimal("-100.00"));
    }

    @Test
    public void testWithdraw() throws BankingException, SQLException {
        // Arrange
        BigDecimal withdrawAmount = new BigDecimal("300.00");
        BigDecimal expectedBalance = new BigDecimal("700.00");

        // Act
        accountService.withdraw(savingsAccountId, withdrawAmount);

        // Assert
        assertEquals(expectedBalance, accountService.getBalance(savingsAccountId));
    }

    @Test(expected = InsufficientFundsException.class)
    public void testWithdrawInsufficientFunds() throws BankingException, SQLException {
        accountService.withdraw(savingsAccountId, new BigDecimal("2000.00"));
    }

    @Test(expected = BankingException.class)
    public void testWithdrawNegativeAmount() throws BankingException, SQLException {
        accountService.withdraw(savingsAccountId, new BigDecimal("-100.00"));
    }

    @Test
    public void testSuccessfulTransfer() throws BankingException, SQLException {
        // Arrange
        BigDecimal transferAmount = new BigDecimal("200.00");
        BigDecimal expectedSavingsBalance = new BigDecimal("800.00");
        BigDecimal expectedCheckingBalance = new BigDecimal("700.00");

        // Act
        accountService.transfer(savingsAccountId, checkingAccountId, transferAmount);

        // Assert
        assertEquals(expectedSavingsBalance, accountService.getBalance(savingsAccountId));
        assertEquals(expectedCheckingBalance, accountService.getBalance(checkingAccountId));
    }

    @Test(expected = InsufficientFundsException.class)
    public void testTransferWithInsufficientFunds() throws BankingException, SQLException {
        accountService.transfer(savingsAccountId, checkingAccountId, new BigDecimal("2000.00"));
    }

    @Test(expected = AccountNotFoundException.class)
    public void testTransferWithInvalidAccount() throws BankingException, SQLException {
        accountService.transfer("INVALID", checkingAccountId, new BigDecimal("100.00"));
    }

    @Test(expected = BankingException.class)
    public void testTransferWithNegativeAmount() throws BankingException, SQLException {
        accountService.transfer(savingsAccountId, checkingAccountId, new BigDecimal("-100.00"));
    }

    @Test
    public void testGetBalance() throws BankingException, SQLException {
        assertEquals(new BigDecimal("1000.00"), accountService.getBalance(savingsAccountId));
    }

    @Test(expected = AccountNotFoundException.class)
    public void testGetBalanceInvalidAccount() throws BankingException, SQLException {
        accountService.getBalance("INVALID");
    }

    @Test
    public void testTransactionHistory() throws BankingException, SQLException {
        // Arrange
        accountService.deposit(savingsAccountId, new BigDecimal("500.00"));
        accountService.withdraw(savingsAccountId, new BigDecimal("200.00"));

        // Act
        List<String> history = accountService.getTransactionHistory(savingsAccountId);

        // Assert
        assertNotNull(history);
        assertTrue(history.size() >= 3); // Initial deposit + deposit + withdraw
    }

    @Test
    public void testGetAllTransactions() throws BankingException, SQLException {
        // Arrange
        accountService.deposit(savingsAccountId, new BigDecimal("500.00"));
        accountService.deposit(checkingAccountId, new BigDecimal("300.00"));

        // Act
        List<String> allTransactions = accountService.getAllTransactions();

        // Assert
        assertNotNull(allTransactions);
        assertTrue(allTransactions.size() >= 4); // Initial deposits + new deposits
    }
}
