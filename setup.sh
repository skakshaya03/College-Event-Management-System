#!/bin/bash

echo "========================================="
echo "  College Event Management System Setup"
echo "========================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Step 1: Check MySQL
echo "Step 1: Checking MySQL..."
if mysql -u root -e "SELECT 1" &> /dev/null; then
    echo -e "${GREEN}✓ MySQL is running${NC}"
else
    echo -e "${YELLOW}⚠ MySQL check failed. You may need to enter password.${NC}"
fi

# Step 2: Setup Database
echo ""
echo "Step 2: Setting up database..."
echo -e "${YELLOW}Please enter your MySQL password if prompted:${NC}"
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
    status ENUM('pending', 'approved', 'rejected') DEFAULT 'pending',
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

-- Insert default users if not exists
INSERT IGNORE INTO users (username, password, full_name, email, role) 
VALUES 
('admin', 'admin123', 'System Admin', 'admin@college.edu', 'admin'),
('john_organizer', 'pass123', 'John Doe', 'john@college.edu', 'organizer'),
('alice_student', 'pass123', 'Alice Smith', 'alice@college.edu', 'student');

SELECT 'Database setup complete!' as status;
EOF

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Database created successfully!${NC}"
else
    echo -e "${RED}✗ Database setup failed!${NC}"
    exit 1
fi

# Step 3: Information
echo ""
echo "========================================="
echo -e "${GREEN}✓ Setup Complete!${NC}"
echo "========================================="
echo ""
echo "📝 Default Login Credentials:"
echo "   Admin:     username: admin          password: admin123"
echo "   Organizer: username: john_organizer password: pass123"
echo "   Student:   username: alice_student  password: pass123"
echo ""
echo "⚙️  To run the full application, you need:"
echo "   1. Apache Tomcat (download from: https://tomcat.apache.org/)"
echo "   2. MySQL Connector JAR (download from: https://dev.mysql.com/downloads/connector/j/)"
echo "   3. JSON Library JAR (org.json)"
echo ""
echo "📖 For detailed setup instructions, see README.md"
echo ""
echo "🚀 Quick Demo Mode (Frontend only):"
echo "   Run: ./demo.sh"
echo ""
