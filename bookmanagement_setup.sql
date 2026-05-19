-- ============================================================
-- Book Management System — MySQL Setup Script
-- Run this in MySQL Workbench or phpMyAdmin before launching
-- ============================================================

-- 1. Create and use the database
CREATE DATABASE IF NOT EXISTS bookmanagement;
USE bookmanagement;

-- 2. Create tblusers table
CREATE TABLE IF NOT EXISTS tblusers (
    userid   INT(6)        NOT NULL AUTO_INCREMENT,
    username VARCHAR(10)   NOT NULL,
    password VARCHAR(10)   NOT NULL,
    PRIMARY KEY (userid),
    UNIQUE KEY uk_username (username)
);

-- 3. Create tblbooks table
CREATE TABLE IF NOT EXISTS tblbooks (
    bookid   INT(6)        NOT NULL AUTO_INCREMENT,
    title    VARCHAR(50)   NOT NULL,
    author   VARCHAR(50)   NOT NULL,
    category VARCHAR(50)   NOT NULL,
    status   VARCHAR(7)    NOT NULL DEFAULT 'Availab',
    PRIMARY KEY (bookid)
);

-- 4. Insert a default admin user (username: admin, password: admin)
INSERT IGNORE INTO tblusers (username, password) VALUES ('admin', 'admin');

-- 5. Insert sample books
INSERT INTO tblbooks (title, author, category, status) VALUES
('The Alchemist',        'Paulo Coelho',    'Fiction',    'Available'),
('Clean Code',           'Robert Martin',   'Technology', 'Borrow'),
('Atomic Habits',        'James Clear',     'Self-Help',  'Available'),
('Harry Potter',         'J.K. Rowling',    'Fiction',    'Available'),
('The Pragmatic Prog',   'Andrew Hunt',     'Technology', 'Borrow');

-- Verify
SELECT * FROM tblusers;
SELECT * FROM tblbooks;
