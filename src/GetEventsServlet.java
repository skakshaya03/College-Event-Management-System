import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import org.json.*;

public class GetEventsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login.html");
            return;
        }
        
        String role = (String) session.getAttribute("role");
        int userId = (Integer) session.getAttribute("userId");
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql;
            
            if ("admin".equals(role)) {
                sql = "SELECT e.*, u.full_name as organizer_name FROM events e JOIN users u ON e.organizer_id = u.id ORDER BY e.created_at DESC";
                stmt = conn.prepareStatement(sql);
            } else if ("organizer".equals(role)) {
                sql = "SELECT e.*, u.full_name as organizer_name FROM events e JOIN users u ON e.organizer_id = u.id WHERE e.organizer_id = ? ORDER BY e.created_at DESC";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
            } else {
                sql = "SELECT e.*, u.full_name as organizer_name FROM events e JOIN users u ON e.organizer_id = u.id WHERE e.status = 'approved' ORDER BY e.created_at DESC";
                stmt = conn.prepareStatement(sql);
            }
            
            rs = stmt.executeQuery();
            JSONArray events = new JSONArray();
            
            while (rs.next()) {
                JSONObject event = new JSONObject();
                event.put("id", rs.getInt("id"));
                event.put("title", rs.getString("title"));
                event.put("description", rs.getString("description"));
                event.put("eventDate", rs.getString("event_date"));
                event.put("venue", rs.getString("venue"));
                event.put("organizerName", rs.getString("organizer_name"));
                event.put("status", rs.getString("status"));
                events.put(event);
            }
            
            response.setContentType("application/json");
            response.getWriter().write(events.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(500);
        } finally {
            DatabaseConnection.closeConnection(conn, stmt, rs);
        }
    }
}
