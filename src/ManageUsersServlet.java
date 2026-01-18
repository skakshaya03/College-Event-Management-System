import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import org.json.*;

public class ManageUsersServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT id, username, full_name, email, role FROM users ORDER BY created_at DESC";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            JSONArray users = new JSONArray();
            while (rs.next()) {
                JSONObject user = new JSONObject();
                user.put("id", rs.getInt("id"));
                user.put("username", rs.getString("username"));
                user.put("fullName", rs.getString("full_name"));
                user.put("email", rs.getString("email"));
                user.put("role", rs.getString("role"));
                users.put(user);
            }
            
            response.setContentType("application/json");
            response.getWriter().write(users.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(500);
        } finally {
            DatabaseConnection.closeConnection(conn, stmt, rs);
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }
        
        String action = request.getParameter("action");
        int userId = Integer.parseInt(request.getParameter("userId"));
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            
            if ("delete".equals(action)) {
                String sql = "DELETE FROM users WHERE id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            } else if ("changeRole".equals(action)) {
                String newRole = request.getParameter("newRole");
                String sql = "UPDATE users SET role = ? WHERE id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, newRole);
                stmt.setInt(2, userId);
                stmt.executeUpdate();
            }
            
            response.sendRedirect("admin_dashboard.html?success=updated");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("admin_dashboard.html?error=failed");
        } finally {
            DatabaseConnection.closeConnection(conn, stmt, null);
        }
    }
}
