import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyServer {
    public static void main(String[] args) {
        Server server = new Server(8080);
        
        try {
            // Setup WebApp context
            WebAppContext webapp = new WebAppContext();
            webapp.setContextPath("/");
            webapp.setResourceBase("web");
            webapp.setDescriptor("web/WEB-INF/web.xml");
            
            // Add compiled classes to classpath
            webapp.setExtraClasspath("target/classes");
            
            server.setHandler(webapp);
            
            System.out.println("=========================================");
            System.out.println("College Event Management System");
            System.out.println("=========================================");
            System.out.println("");
            System.out.println("✅ Server starting on http://localhost:8080");
            System.out.println("");
            System.out.println("📌 Access points:");
            System.out.println("   - Login: http://localhost:8080/login.html");
            System.out.println("   - Register: http://localhost:8080/register.html");
            System.out.println("");
            System.out.println("📝 Default users:");
            System.out.println("   - admin / admin123 (Admin)");
            System.out.println("   - john_organizer / pass123 (Organizer)");
            System.out.println("   - alice_student / pass123 (Student)");
            System.out.println("");
            System.out.println("Press Ctrl+C to stop the server");
            System.out.println("=========================================");
            
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
