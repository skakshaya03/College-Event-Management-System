#!/bin/bash

echo "========================================="
echo "  Starting Demo Mode (Frontend Only)"
echo "========================================="
echo ""
echo "⚠️  NOTE: This is a FRONTEND PREVIEW only!"
echo "   The backend servlets are NOT running."
echo "   You'll see the UI but login/features won't work."
echo ""
echo "📖 To run the FULL APPLICATION with Java backend:"
echo "   1. Install Apache Tomcat"
echo "   2. Follow instructions in README.md"
echo ""
echo "🌐 Opening browser at: http://localhost:8000"
echo "📁 Serving files from: web/"
echo ""
echo "Press Ctrl+C to stop the server"
echo "========================================="
echo ""

cd web

# Try to open browser
sleep 2 && open http://localhost:8000 2>/dev/null &

# Start Python HTTP server
python3 -m http.server 8000
