import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class ApproveEventServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }
        
        int eventId = Integer.parseInt(request.getParameter("eventId"));
        String action = request.getParameter("action");
        String status = "approved".equals(action) ? "approved" : "rejected";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "UPDATE events SET status = ? WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setInt(2, eventId);
            
            stmt.executeUpdate();
            response.sendRedirect("admin_dashboard.html?success=updated");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("admin_dashboard.html?error=failed");
        } finally {
            DatabaseConnection.closeConnection(conn, stmt, null);
        }
    }
}
