-- Users Table
CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100),
    iban VARCHAR(34),
    balance DECIMAL(15, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login_date TIMESTAMP,
    consecutive_login_days INTEGER DEFAULT 0
);

-- Transactions Table
CREATE TABLE IF NOT EXISTS transactions (
    transaction_identifier VARCHAR(100) PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    category VARCHAR(100),
    receiver_iban VARCHAR(34),
    receiver_name VARCHAR(100),
    receiver_account_id VARCHAR(50),
    description TEXT,
    datetime TIMESTAMP,
    status VARCHAR(20),
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

-- Chicken Table
CREATE TABLE IF NOT EXISTS chicken (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    level INTEGER DEFAULT 1,
    experience INTEGER DEFAULT 0,
    last_fed TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

-- Challenge Table
CREATE TABLE IF NOT EXISTS challenge (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    difficulty VARCHAR(10) NOT NULL,
    xp_reward INTEGER NOT NULL,
    duration_days INTEGER NOT NULL,
    limit_amount DECIMAL(15, 2) NOT NULL,
    spent_amount DECIMAL(15, 2) DEFAULT 0.0 NOT NULL,
    category VARCHAR(100) NOT NULL,
    type VARCHAR(10) NOT NULL, -- SPEND or DONT_SPEND
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    status VARCHAR(10) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
); 