import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class GenerateQRServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String eventIdParam = request.getParameter("eventId");
        if (eventIdParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int eventId = Integer.parseInt(eventIdParam);
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            
            // Get event details
            String sql = "SELECT title, venue, event_date FROM events WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, eventId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                String title = rs.getString("title");
                String venue = rs.getString("venue");
                String eventDate = rs.getString("event_date");
                
                // Create QR code content
                String qrContent = "Event: " + title + "\nVenue: " + venue + "\nDate: " + eventDate;
                
                // Generate QR Code
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300);
                
                // Write to response
                response.setContentType("image/png");
                MatrixToImageWriter.writeToStream(bitMatrix, "PNG", response.getOutputStream());
                
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            DatabaseConnection.closeConnection(conn, stmt, rs);
        }
    }
}
