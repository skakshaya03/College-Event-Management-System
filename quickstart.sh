#!/bin/bash

echo "========================================="
echo "  College Event Management System"
echo "  SQLite Edition - No Password Needed!"
echo "========================================="
echo ""

echo "🔨 Building application with SQLite..."
mvn clean compile

if [ $? -ne 0 ]; then
    echo "❌ Build failed!"
    exit 1
fi

echo ""
echo "========================================="
echo "🚀 Starting Application"
echo "========================================="
echo ""
echo "📝 Default Login Credentials:"
echo "   👨‍💼 Admin:     admin / admin123"
echo "   📅 Organizer: john_organizer / pass123"
echo "   🎓 Student:   alice_student / pass123"
echo ""
echo "🌐 Server: http://localhost:8080"
echo "Press Ctrl+C to stop"
echo "========================================="
echo ""

sleep 3 && open http://localhost:8080 2>/dev/null &
mvn jetty:run
