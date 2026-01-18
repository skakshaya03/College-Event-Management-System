import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import org.json.*;

public class GetRegistrationsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"organizer".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }
        
        int eventId = Integer.parseInt(request.getParameter("eventId"));
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT u.full_name, u.email, u.department, u.year, u.phone, r.registered_at FROM registrations r JOIN users u ON r.student_id = u.id WHERE r.event_id = ? ORDER BY r.registered_at DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, eventId);
            rs = stmt.executeQuery();
            
            JSONArray registrations = new JSONArray();
            while (rs.next()) {
                JSONObject reg = new JSONObject();
                reg.put("fullName", rs.getString("full_name"));
                reg.put("email", rs.getString("email"));
                reg.put("department", rs.getString("department"));
                reg.put("year", rs.getString("year"));
                reg.put("phone", rs.getString("phone"));
                reg.put("registeredAt", rs.getString("registered_at"));
                registrations.put(reg);
            }
            
            response.setContentType("application/json");
            response.getWriter().write(registrations.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(500);
        } finally {
            DatabaseConnection.closeConnection(conn, stmt, rs);
        }
    }
}
