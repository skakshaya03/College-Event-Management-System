import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                HttpSession session = request.getSession();
                session.setAttribute("userId", rs.getInt("id"));
                session.setAttribute("username", rs.getString("username"));
                session.setAttribute("fullName", rs.getString("full_name"));
                session.setAttribute("role", rs.getString("role"));
                
                String role = rs.getString("role");
                System.out.println("✅ Login successful: " + username + " as " + role);
                if ("admin".equals(role)) {
                    response.sendRedirect("admin_dashboard.html");
                } else if ("organizer".equals(role)) {
                    response.sendRedirect("organizer_dashboard.html");
                } else {
                    response.sendRedirect("student_dashboard.html");
                }
            } else {
                System.out.println("❌ Login failed: Invalid credentials for " + username);
                response.sendRedirect("login.html?error=invalid");
            }
        } catch (SQLException e) {
            System.out.println("❌ Login error: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("login.html?error=server");
        } finally {
            DatabaseConnection.closeConnection(conn, stmt, rs);
        }
    }
}
