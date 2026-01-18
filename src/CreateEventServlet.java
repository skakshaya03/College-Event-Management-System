import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class CreateEventServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"organizer".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }
        
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String eventDate = request.getParameter("eventDate");
        String venue = request.getParameter("venue");
        int organizerId = (Integer) session.getAttribute("userId");
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO events (title, description, event_date, venue, organizer_id, status) VALUES (?, ?, ?, ?, ?, 'pending')";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setString(3, eventDate);
            stmt.setString(4, venue);
            stmt.setInt(5, organizerId);
            
            stmt.executeUpdate();
            response.sendRedirect("organizer_dashboard.html?success=created");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("organizer_dashboard.html?error=failed");
        } finally {
            DatabaseConnection.closeConnection(conn, stmt, null);
        }
    }
}
