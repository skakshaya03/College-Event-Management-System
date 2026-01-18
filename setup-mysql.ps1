# MySQL Database Setup Script for Windows
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "  MySQL Database Setup" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host ""

# Prompt for MySQL password
$mysqlPassword = Read-Host "Enter your MySQL root password" -AsSecureString
$BSTR = [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($mysqlPassword)
$plainPassword = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto($BSTR)

# SQL script content
$sqlScript = @"
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
"@

# Save SQL script to temp file
$tempSqlFile = Join-Path $env:TEMP "setup_db.sql"
$sqlScript | Out-File -FilePath $tempSqlFile -Encoding utf8

# Execute MySQL command
Write-Host "Setting up database..." -ForegroundColor Yellow
$mysqlPath = "C:\Program Files\MySQL\MySQL Server 9.4\bin\mysql.exe"
try {
    # Use Get-Content to pipe SQL to MySQL
    Get-Content $tempSqlFile | & $mysqlPath -u root -p"$plainPassword"
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "=========================================" -ForegroundColor Green
        Write-Host "✅ MySQL Database Setup Complete!" -ForegroundColor Green
        Write-Host "=========================================" -ForegroundColor Green
        Write-Host ""
        Write-Host "📝 Default users created:" -ForegroundColor Cyan
        Write-Host "   - Username: admin | Password: admin123 | Role: admin" -ForegroundColor White
        Write-Host "   - Username: john_organizer | Password: pass123 | Role: organizer" -ForegroundColor White
        Write-Host "   - Username: alice_student | Password: pass123 | Role: student" -ForegroundColor White
        Write-Host ""
        Write-Host "📌 Next step: Set MYSQL_PASSWORD environment variable" -ForegroundColor Yellow
        Write-Host "   Run: `$env:MYSQL_PASSWORD = 'your_password'" -ForegroundColor White
    } else {
        Write-Host ""
        Write-Host "❌ Setup failed. Please check your MySQL password and try again." -ForegroundColor Red
    }
} catch {
    Write-Host ""
    Write-Host "❌ Error: $_" -ForegroundColor Red
} finally {
    # Clean up temp file
    if (Test-Path $tempSqlFile) {
        Remove-Item $tempSqlFile -Force
    }
}
