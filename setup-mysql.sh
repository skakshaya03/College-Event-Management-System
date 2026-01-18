#!/bin/bash

echo "========================================="
echo "  MySQL Database Setup"
echo "========================================="
echo ""
echo "Please enter your MySQL root password when prompted:"
echo ""

mysql -u root -p << 'EOF'
-- Create database
CREATE DATABASE IF NOT EXISTS college_events;
USE college_events;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role ENUM('student', 'organizer', 'admin') NOT NULL,
    department VARCHAR(100),
    year VARCHAR(20),
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Events table
CREATE TABLE IF NOT EXISTS events (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    event_date DATE NOT NULL,
    venue VARCHAR(200),
    organizer_id INT NOT NULL,
    status ENUM('pending', 'approved', 'rejected', 'completed') DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (organizer_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Event registrations table
CREATE TABLE IF NOT EXISTS registrations (
    id INT PRIMARY KEY AUTO_INCREMENT,
    event_id INT NOT NULL,
    student_id INT NOT NULL,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_registration (event_id, student_id)
);

-- Insert default users
INSERT IGNORE INTO users (username, password, full_name, email, role) 
VALUES 
('admin', 'admin123', 'System Admin', 'admin@college.edu', 'admin'),
('john_organizer', 'pass123', 'John Doe', 'john@college.edu', 'organizer'),
('alice_student', 'pass123', 'Alice Smith', 'alice@college.edu', 'student');

SELECT '✅ Database setup complete!' as Status;
EOF

if [ $? -eq 0 ]; then
    echo ""
    echo "========================================="
    echo "✅ MySQL Database Setup Complete!"
    echo "========================================="
    echo ""
    echo "📝 Update your MySQL password in:"
    echo "   src/DatabaseConnection.java"
    echo ""
    echo "Then run: ./start-mysql.sh"
else
    echo ""
    echo "❌ Setup failed. Please check your MySQL password."
fi
