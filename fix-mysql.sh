#!/bin/bash

echo "========================================="
echo "  MySQL Password Reset Guide"
echo "========================================="
echo ""
echo "Since you've forgotten your MySQL password, here are your options:"
echo ""
echo "Option 1: Reset MySQL Password (Recommended)"
echo "----------------------------------------"
echo "Run these commands:"
echo ""
echo "  1. Stop MySQL:"
echo "     brew services stop mysql"
echo ""
echo "  2. Start MySQL in safe mode:"
echo "     mysqld_safe --skip-grant-tables &"
echo ""
echo "  3. Connect without password:"
echo "     mysql -u root"
echo ""
echo "  4. Reset password (in MySQL prompt):"
echo "     FLUSH PRIVILEGES;"
echo "     ALTER USER 'root'@'localhost' IDENTIFIED BY 'newpassword';"
echo "     exit;"
echo ""
echo "  5. Restart MySQL normally:"
echo "     killall mysqld"
echo "     brew services start mysql"
echo ""
echo "========================================="
echo ""
echo "Option 2: Use SQLite Instead (Quick & Easy)"
echo "----------------------------------------"
echo "We can switch to SQLite which doesn't need a password!"
echo ""
read -p "Would you like to switch to SQLite? (y/n): " choice

if [ "$choice" = "y" ] || [ "$choice" = "Y" ]; then
    echo ""
    echo "🔄 Converting to SQLite..."
    echo "This will modify the application to use SQLite instead of MySQL."
    echo ""
    read -p "Press Enter to continue or Ctrl+C to cancel..."
    
    # Will be implemented if user chooses this option
    echo "Please run: ./convert-to-sqlite.sh"
else
    echo ""
    echo "Please reset your MySQL password using Option 1 above,"
    echo "then run: ./start.sh"
fi

echo ""
