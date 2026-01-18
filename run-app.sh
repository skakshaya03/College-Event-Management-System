#!/bin/bash

echo "========================================="
echo "  College Event Management System"
echo "========================================="
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed!"
    echo ""
    echo "Please install Maven first:"
    echo "  brew install maven"
    echo ""
    exit 1
fi

echo "✅ Maven found: $(mvn -version | head -n 1)"
echo ""

# Check MySQL
echo "Checking MySQL connection..."
if mysql -u root -e "SELECT 1" &> /dev/null; then
    echo "✅ MySQL is running"
else
    echo "⚠️  Cannot connect to MySQL without password"
    echo "   Attempting to setup database..."
fi

echo ""
echo "Setting up database..."
mysql -u root -p college_events < database/schema.sql 2>/dev/null || {
    echo ""
    echo "📝 Note: If database setup failed, run manually:"
    echo "   mysql -u root -p < database/schema.sql"
    echo ""
}

# Update database credentials if needed
echo ""
echo "⚙️  Database Configuration:"
echo "   Edit src/DatabaseConnection.java if you need to change:"
echo "   - Database URL (default: localhost:3306/college_events)"
echo "   - Username (default: root)"
echo "   - Password (default: empty)"
echo ""

# Build and run
echo "========================================="
echo "🔨 Building application..."
echo "========================================="
echo ""

mvn clean compile

if [ $? -ne 0 ]; then
    echo ""
    echo "❌ Build failed! Please check the errors above."
    exit 1
fi

echo ""
echo "========================================="
echo "🚀 Starting server..."
echo "========================================="
echo ""
echo "📝 Default Login Credentials:"
echo "   Admin:     username: admin          password: admin123"
echo "   Organizer: username: john_organizer password: pass123"  
echo "   Student:   username: alice_student  password: pass123"
echo ""
echo "🌐 Server will start at: http://localhost:8080"
echo "   Press Ctrl+C to stop"
echo ""
echo "========================================="
echo ""

# Run with Jetty
mvn jetty:run
