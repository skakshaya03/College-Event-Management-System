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

public class GetAllRegistrationsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        int organizerId = (int) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");
        
        if (!"organizer".equals(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        response.setContentType("application/json");
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            
            // Get all registrations for events created by this organizer
            String sql = "SELECT e.title as eventTitle, u.full_name as fullName, u.email, " +
                        "u.department, u.year, u.phone, r.registered_at as registeredAt " +
                        "FROM registrations r " +
                        "JOIN events e ON r.event_id = e.id " +
                        "JOIN users u ON r.student_id = u.id " +
                        "WHERE e.organizer_id = ? " +
                        "ORDER BY e.title, r.registered_at DESC";
            
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, organizerId);
            rs = stmt.executeQuery();
            
            JSONArray registrations = new JSONArray();
            
            while (rs.next()) {
                JSONObject reg = new JSONObject();
                reg.put("eventTitle", rs.getString("eventTitle"));
                reg.put("fullName", rs.getString("fullName"));
                reg.put("email", rs.getString("email"));
                reg.put("department", rs.getString("department"));
                reg.put("year", rs.getString("year"));
                reg.put("phone", rs.getString("phone"));
                reg.put("registeredAt", rs.getTimestamp("registeredAt").toString());
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
