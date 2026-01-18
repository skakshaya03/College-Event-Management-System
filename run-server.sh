#!/bin/bash

echo "========================================="
echo "  Run Application (Skip DB Setup)"
echo "========================================="
echo ""
echo "⚠️  IMPORTANT: Make sure you have:"
echo "   1. Created the database 'college_events'"
echo "   2. Run the schema.sql file"
echo "   3. Updated MySQL password in src/DatabaseConnection.java"
echo ""
echo "To setup database manually, run:"
echo "   mysql -u root -p < database/schema.sql"
echo ""
read -p "Press Enter to start the server (or Ctrl+C to cancel)..."

echo ""
echo "========================================="
echo "🚀 Starting Application Server"
echo "========================================="
echo ""
echo "📝 Default Login Credentials:"
echo "   👨‍💼 Admin:     admin / admin123"
echo "   📅 Organizer: john_organizer / pass123"
echo "   🎓 Student:   alice_student / pass123"
echo ""
echo "🌐 Server starting at: http://localhost:8080"
echo ""
echo "Press Ctrl+C to stop the server"
echo "========================================="
echo ""

# Wait and open browser
sleep 3 && open http://localhost:8080 2>/dev/null &

# Start server
mvn jetty:run
