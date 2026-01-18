# Set MySQL Password
$env:MYSQL_PASSWORD = "root123"

Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "College Event Management System" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "✅ Starting server on http://localhost:8080" -ForegroundColor Green
Write-Host ""
Write-Host "📌 Access points:" -ForegroundColor Yellow
Write-Host "   - Login: http://localhost:8080/login.html" -ForegroundColor White
Write-Host "   - Register: http://localhost:8080/register.html" -ForegroundColor White
Write-Host ""
Write-Host "📝 Default users:" -ForegroundColor Yellow
Write-Host "   - admin / admin123 (Admin)" -ForegroundColor White
Write-Host "   - john_organizer / pass123 (Organizer)" -ForegroundColor White
Write-Host "   - alice_student / pass123 (Student)" -ForegroundColor White
Write-Host ""
Write-Host "Press Ctrl+C to stop the server" -ForegroundColor Red
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host ""

# Run using Maven exec plugin
mvn exec:java -D"exec.mainClass"="JettyServer"
