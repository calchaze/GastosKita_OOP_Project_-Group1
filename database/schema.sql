-- GastosKita database (the app also creates this automatically on first run)
CREATE DATABASE IF NOT EXISTS gastoskita;
USE gastoskita;

CREATE TABLE IF NOT EXISTS budget (
    id           INT PRIMARY KEY,
    total_budget DECIMAL(12,2) NOT NULL DEFAULT 0
);
INSERT IGNORE INTO budget (id, total_budget) VALUES (1, 0);

CREATE TABLE IF NOT EXISTS expenses (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    amount       DECIMAL(12,2) NOT NULL,
    category     VARCHAR(30)   NOT NULL,
    description  VARCHAR(255)  NOT NULL,
    expense_date DATE          NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
