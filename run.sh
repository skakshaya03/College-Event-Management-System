#!/bin/bash

echo "==================================="
echo "College Event Management System"
echo "==================================="
echo ""

# Check if MySQL is running
echo "Checking MySQL server..."
if ! mysql -u root -e "SELECT 1" &> /dev/null; then
    echo "⚠️  MySQL is not running or credentials are wrong."
    echo "Please start MySQL server and ensure you can connect with:"
    echo "  mysql -u root -p"
    echo ""
    read -p "Press Enter to continue anyway or Ctrl+C to exit..."
fi

# Setup database
echo ""
echo "Setting up database..."
echo "Please enter your MySQL root password when prompted:"
mysql -u root -p < database/schema.sql 2>/dev/null || {
    echo "⚠️  Database setup may have failed. Continuing anyway..."
}

echo ""
echo "✅ Database setup complete!"
echo ""
echo "Now, let's run a simple Python HTTP server to serve the frontend."
echo "Note: This is a simplified version. For full functionality, you need:"
echo "  1. Apache Tomcat installed"
echo "  2. Compiled servlets"
echo "  3. Required JAR files (MySQL connector, JSON library)"
echo ""
echo "Starting development server on http://localhost:8000"
echo "Press Ctrl+C to stop the server"
echo ""

cd web
python3 -m http.server 8000
