#!/bin/bash

echo "========================================="
echo "  Quick Start - College Event System"
echo "========================================="
echo ""

# Step 1: Setup database (interactive)
echo "📊 Step 1: Database Setup"
echo "----------------------------------------"
echo "Please enter your MySQL root password to setup the database:"
echo ""

mysql -u root -p << 'EOF'
CREATE DATABASE IF NOT EXISTS college_events;
USE college_events;

CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role ENUM('student', 'organizer', 'admin') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

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

CREATE TABLE IF NOT EXISTS registrations (
    id INT PRIMARY KEY AUTO_INCREMENT,
    event_id INT NOT NULL,
    student_id INT NOT NULL,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_registration (event_id, student_id)
);

INSERT IGNORE INTO users (username, password, full_name, email, role) 
VALUES 
('admin', 'admin123', 'System Admin', 'admin@college.edu', 'admin'),
('john_organizer', 'pass123', 'John Doe', 'john@college.edu', 'organizer'),
('alice_student', 'pass123', 'Alice Smith', 'alice@college.edu', 'student');

SELECT '✅ Database setup complete!' as status;
EOF

if [ $? -ne 0 ]; then
    echo ""
    echo "❌ Database setup failed!"
    echo "Please check your MySQL password and try again."
    exit 1
fi

echo ""
echo "========================================="
echo "🚀 Step 2: Starting Application Server"
echo "========================================="
echo ""
echo "📝 Default Login Credentials:"
echo "   👨‍💼 Admin:     admin / admin123"
echo "   📅 Organizer: john_organizer / pass123"
echo "   🎓 Student:   alice_student / pass123"
echo ""
echo "🌐 Opening: http://localhost:8080"
echo ""
echo "⚠️  Note: If you changed your MySQL password from default,"
echo "   update it in: src/DatabaseConnection.java"
echo "   Then press Ctrl+C and run this script again."
echo ""
echo "Press Ctrl+C to stop the server"
echo "========================================="
echo ""

# Wait a bit then open browser
sleep 3 && open http://localhost:8080 2>/dev/null &

# Start the server
mvn jetty:run
