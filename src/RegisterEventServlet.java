import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class RegisterEventServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"student".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }
        
        int eventId = Integer.parseInt(request.getParameter("eventId"));
        int studentId = (Integer) session.getAttribute("userId");
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO registrations (event_id, student_id) VALUES (?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, eventId);
            stmt.setInt(2, studentId);
            
            stmt.executeUpdate();
            response.sendRedirect("student_dashboard.html?success=registered");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("student_dashboard.html?error=already");
        } finally {
            DatabaseConnection.closeConnection(conn, stmt, null);
        }
    }
}
