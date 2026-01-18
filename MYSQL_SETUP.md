# MySQL Setup Guide

## ⚠️ Important: MySQL Password Required

Your application is now configured to use MySQL instead of SQLite.

## Step 1: Reset MySQL Root Password (if needed)

If you forgot your MySQL root password, reset it:

```bash
# Stop MySQL
brew services stop mysql

# Start MySQL in safe mode
mysqld_safe --skip-grant-tables &

# Connect without password
mysql -u root

# Reset password (in MySQL prompt)
FLUSH PRIVILEGES;
ALTER USER 'root'@'localhost' IDENTIFIED BY 'your_new_password';
FLUSH PRIVILEGES;
exit;

# Restart MySQL normally
killall mysqld
brew services start mysql
```

## Step 2: Create Database

Once you have your MySQL password, run:

```bash
mysql -u root -p
```

Then paste this SQL:

```sql
-- Create database
DROP DATABASE IF EXISTS college_events;
CREATE DATABASE college_events;
USE college_events;

-- Users table
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role ENUM('student', 'organizer', 'admin') NOT NULL,
    department VARCHAR(100),
    year VARCHAR(20),
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Events table
CREATE TABLE events (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    event_date DATE NOT NULL,
    venue VARCHAR(200),
    organizer_id INT NOT NULL,
    status ENUM('pending', 'approved', 'rejected') DEFAULT 'pending',
    kanban_status ENUM('todo', 'completed') DEFAULT 'todo',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (organizer_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Registrations table
CREATE TABLE registrations (
    id INT PRIMARY KEY AUTO_INCREMENT,
    event_id INT NOT NULL,
    student_id INT NOT NULL,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_registration (event_id, student_id)
);

-- Insert default users
INSERT INTO users (username, password, full_name, email, role) VALUES 
    ('admin', 'admin123', 'System Admin', 'admin@college.edu', 'admin'),
    ('john_organizer', 'pass123', 'John Doe', 'john@college.edu', 'organizer'),
    ('alice_student', 'pass123', 'Alice Smith', 'alice@college.edu', 'student');

-- Verify
SELECT username, role FROM users;
```

Type `exit;` to quit MySQL.

## Step 3: Set MySQL Password for Java

Before starting the application, set your MySQL password:

```bash
export MYSQL_PASSWORD="your_mysql_password"
```

## Step 4: Build and Run

```bash
# Build the project
mvn clean package

# Start the server (make sure MYSQL_PASSWORD is set)
mvn jetty:run
```

## Alternative: Use Empty Password

If your MySQL root has no password, just run directly:

```bash
mvn jetty:run
```

## Verify Database

Check if database is working:

```bash
mysql -u root -p
USE college_events;
SHOW TABLES;
SELECT * FROM users;
```

You should see 3 default users: admin, john_organizer, alice_student
