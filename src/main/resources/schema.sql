CREATE DATABASE IF NOT EXISTS onlineShop;
USE onlineShop;

CREATE TABLE IF NOT EXISTS Products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    discontinued BOOLEAN DEFAULT FALSE
);

-- Insert some sample products
INSERT INTO Products (name, price) VALUES
('Kaffe', 49.90),
('Te', 39.90),
('Coca Cola', 19.90),
('Bröd', 29.90),
('Mjölk', 15.90)
ON DUPLICATE KEY UPDATE name=name;
