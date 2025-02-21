-- Store account information
CREATE TABLE accounts (
    account_id INT AUTO_INCREMENT  PRIMARY KEY,
     account_number VARCHAR(20) NOT NULL,
    account_type VARCHAR(20) NOT NULL,
    balance INT NOT NULL
);

-- Store all transactions
CREATE TABLE transactions (
    transaction_ID INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    amount DECIMAL(10,2) NOT NULL,
    balance DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (account_id)
        REFERENCES accounts(account_id)
);

