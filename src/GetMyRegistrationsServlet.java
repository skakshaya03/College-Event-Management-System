import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GetMyRegistrationsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        int studentId = (int) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");
        
        if (!"student".equals(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        response.setContentType("application/json");
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            
            // Get all events the student has registered for
            String sql = "SELECT e.id, e.title, e.description, e.event_date, e.venue, " +
                        "u.full_name as organizerName, r.registered_at " +
                        "FROM registrations r " +
                        "JOIN events e ON r.event_id = e.id " +
                        "JOIN users u ON e.organizer_id = u.id " +
                        "WHERE r.student_id = ? AND e.status = 'approved' " +
                        "ORDER BY e.event_date DESC";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            rs = stmt.executeQuery();
            
            JSONArray registrations = new JSONArray();
            
            while (rs.next()) {
                JSONObject reg = new JSONObject();
                reg.put("id", rs.getInt("id"));
                reg.put("title", rs.getString("title"));
                reg.put("description", rs.getString("description"));
                reg.put("eventDate", rs.getString("event_date"));
                reg.put("venue", rs.getString("venue"));
                reg.put("organizerName", rs.getString("organizerName"));
                reg.put("registeredAt", rs.getTimestamp("registered_at").toString());
                registrations.put(reg);
            }
            
            response.getWriter().write(registrations.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        } finally {
            DatabaseConnection.closeConnection(conn, stmt, rs);
        }
    }
}
