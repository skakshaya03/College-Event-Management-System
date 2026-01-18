import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String role = request.getParameter("role");
        String department = request.getParameter("department");
        String year = request.getParameter("year");
        String phone = request.getParameter("phone");
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO users (username, password, full_name, email, role, department, year, phone) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, fullName);
            stmt.setString(4, email);
            stmt.setString(5, role);
            stmt.setString(6, department);
            stmt.setString(7, year);
            stmt.setString(8, phone);
            
            int rows = stmt.executeUpdate();
            System.out.println("✅ User registered: " + username + " as " + role);
            if (rows > 0) {
                response.sendRedirect("login.html?success=registered");
            } else {
                response.sendRedirect("register.html?error=failed");
            }
        } catch (SQLException e) {
            System.out.println("❌ Registration error: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("register.html?error=exists");
        } finally {
            DatabaseConnection.closeConnection(conn, stmt, null);
        }
    }
}
