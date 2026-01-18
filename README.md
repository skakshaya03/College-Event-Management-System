# College Event Management System 🎓

A complete web application for managing college events with role-based access control built using **Java**, **MySQL**, and **HTML/CSS**.

## 📋 Features

### 👨‍💼 Admin
- Approve/reject events created by organizers
- Manage users (view, delete, change roles)
- Full system oversight

### 📅 Event Organizer
- Create and manage events
- View event registrations with student details
- User management page to see all registrations
- Track registered students (Name, Email, Department, Year, Phone)

### 🎓 Student
- View approved events
- Register for events with detailed registration form
- View registered events with QR codes
- Generate QR codes for event details (scan with phone to view)
- Cannot register for same event twice

## 🛠️ Technology Stack

- **Backend**: Java Servlets
- **Database**: MySQL 8.x
- **Build Tool**: Maven 3.x
- **Server**: Jetty (embedded)
- **Frontend**: HTML5, CSS3, JavaScript
- **QR Generation**: ZXing library

## 📦 Prerequisites

Before you start, make sure you have these installed on your Windows laptop:

1. **Java Development Kit (JDK) 11 or higher**
   - Download from: https://www.oracle.com/java/technologies/downloads/
   - After installation, verify: Open Command Prompt and type `java -version`

2. **Apache Maven**
   - Download from: https://maven.apache.org/download.cgi
   - Extract and add to PATH
   - Verify: `mvn -version`

3. **MySQL Server**
   - Download from: https://dev.mysql.com/downloads/mysql/
   - During installation, set root password (remember this!)
   - Recommended: Install MySQL Workbench for easy management

4. **Git** (to clone the project)
   - Download from: https://git-scm.com/download/win

5. **VS Code** (recommended editor)
   - Download from: https://code.visualstudio.com/
   - Install "Extension Pack for Java" extension

## 🚀 Installation & Setup

### Step 1: Clone or Extract the Project

```bash
# If using Git
git clone <repository-url>
cd java_event

# Or simply extract the ZIP file and open in VS Code
```

### Step 2: Set Up MySQL Database

1. **Open MySQL Command Line** or **MySQL Workbench**

2. **Login to MySQL**:
   ```bash
   mysql -u root -p
   # Enter your MySQL root password
   ```

3. **Create the Database and Tables**:
   ```sql
   -- Create database
   CREATE DATABASE IF NOT EXISTS college_events;
   USE college_events;

   -- Create users table
   CREATE TABLE users (
       id INT PRIMARY KEY AUTO_INCREMENT,
       username VARCHAR(50) UNIQUE NOT NULL,
       password VARCHAR(255) NOT NULL,
       full_name VARCHAR(100) NOT NULL,
       email VARCHAR(100) UNIQUE NOT NULL,
       role ENUM('student', 'organizer', 'admin') NOT NULL,
       department VARCHAR(100),
       year VARCHAR(20),
       phone VARCHAR(20),
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );

   -- Create events table
   CREATE TABLE events (
       id INT PRIMARY KEY AUTO_INCREMENT,
       title VARCHAR(200) NOT NULL,
       description TEXT,
       event_date DATE NOT NULL,
       venue VARCHAR(200),
       organizer_id INT NOT NULL,
       status ENUM('pending', 'approved', 'rejected') DEFAULT 'pending',
       kanban_status ENUM('todo', 'completed') DEFAULT 'todo',
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       FOREIGN KEY (organizer_id) REFERENCES users(id) ON DELETE CASCADE
   );

   -- Create registrations table
   CREATE TABLE registrations (
       id INT PRIMARY KEY AUTO_INCREMENT,
       event_id INT NOT NULL,
       student_id INT NOT NULL,
       registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
       FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
       UNIQUE KEY unique_registration (event_id, student_id)
   );

   -- Insert default users
   INSERT INTO users (username, password, full_name, email, role) VALUES 
       ('admin', 'admin123', 'System Admin', 'admin@college.edu', 'admin'),
       ('john_organizer', 'pass123', 'John Doe', 'john@college.edu', 'organizer'),
       ('alice_student', 'pass123', 'Alice Smith', 'alice@college.edu', 'student');

   -- Verify tables created
   SHOW TABLES;
   SELECT username, role FROM users;
   
   -- Exit MySQL
   exit;
   ```

### Step 3: Configure Database Connection

1. Open the project in VS Code
2. The database connection is already configured in `src/DatabaseConnection.java`
3. **Set your MySQL password as an environment variable**:

   **On Windows Command Prompt:**
   ```cmd
   set MYSQL_PASSWORD=your_mysql_password
   ```

   **On Windows PowerShell:**
   ```powershell
   $env:MYSQL_PASSWORD="your_mysql_password"
   ```

   Replace `your_mysql_password` with your actual MySQL root password.

### Step 4: Build the Project

Open Command Prompt or PowerShell in the project directory:

```bash
# Clean and compile the project
mvn clean compile

# Package the application
mvn package
```

If you see "BUILD SUCCESS", you're good to go!

### Step 5: Run the Application

```bash
# Make sure MYSQL_PASSWORD is set
set MYSQL_PASSWORD=your_mysql_password

# Start the server
mvn jetty:run
```

You should see:
```
[INFO] Started Jetty Server
```

### Step 6: Access the Application

Open your browser and go to: **http://localhost:8080**

## 👥 Default Login Credentials

| Role      | Username         | Password  |
|-----------|------------------|-----------|
| Admin     | admin            | admin123  |
| Organizer | john_organizer   | pass123   |
| Student   | alice_student    | pass123   |

## 🎯 How to Use

### As Admin:
1. Login with admin credentials
2. View pending events and approve/reject them
3. Manage users (view all users, change roles, delete users)

### As Event Organizer:
1. Login with organizer credentials
2. Click **"Create New Event"** to add an event
3. Fill in event details (title, description, date, venue)
4. Event goes to admin for approval
5. Once approved, click **"View Registrations"** to see who registered
6. Click **"User Management"** to see all registrations for all your events

### As Student:
1. Login with student credentials
2. View all approved events
3. Click **"Register for Event"** to open registration form
4. Fill in details (name, email, phone, department, year, reason)
5. Submit registration
6. View your registered events in **"My Registered Events"** section
7. Click **"📱 View QR Code"** to get a scannable QR code
8. Scan QR code with your phone to see event details

## 📱 QR Code Feature

- After registering for an event, students get a QR code
- QR code contains: Event Name, Venue, and Date
- Scan with any QR scanner app on your phone
- Perfect for quick event check-in!

## 🐛 Troubleshooting

### Problem: "Port 8080 is already in use"
**Solution**: Stop any application using port 8080 or change the port in `pom.xml`

### Problem: "Access denied for user 'root'@'localhost'"
**Solution**: 
- Check your MySQL password is correct
- Make sure `MYSQL_PASSWORD` environment variable is set
- Try resetting MySQL root password

### Problem: "ClassNotFoundException" or compilation errors
**Solution**: 
- Run `mvn clean install` to download dependencies
- Make sure Java and Maven are properly installed

### Problem: Database connection fails
**Solution**:
- Verify MySQL service is running (check Windows Services)
- Test connection: `mysql -u root -p`
- Check if database `college_events` exists

### Problem: QR code not showing
**Solution**: 
- Make sure you have registered for an event first
- Check browser console for errors (F12)
- Verify ZXing library is downloaded (check `target/` folder)

## 🔄 Stopping the Server

Press `Ctrl + C` in the terminal where the server is running.

## 📁 Project Structure

```
java_event/
├── src/                          # Java source files
│   ├── DatabaseConnection.java   # MySQL connection handler
│   ├── LoginServlet.java         # User authentication
│   ├── RegisterServlet.java      # User registration
│   ├── CreateEventServlet.java   # Event creation
│   ├── GetEventsServlet.java     # Fetch events
│   ├── ApproveEventServlet.java  # Admin approval
│   ├── RegisterEventServlet.java # Event registration
│   ├── GetRegistrationsServlet.java # View registrations
│   ├── GetAllRegistrationsServlet.java # Organizer registrations
│   ├── GetMyRegistrationsServlet.java # Student's registrations
│   ├── GenerateQRServlet.java    # QR code generation
│   ├── ManageUsersServlet.java   # User management
│   └── LogoutServlet.java        # Session logout
├── web/                          # Frontend files
│   ├── css/
│   │   └── style.css            # Styling
│   ├── login.html               # Login page
│   ├── register.html            # User registration page
│   ├── student_dashboard.html   # Student interface
│   ├── organizer_dashboard.html # Organizer interface
│   ├── admin_dashboard.html     # Admin interface
│   ├── event_registration.html  # Event registration form
│   ├── user_management.html     # Organizer user management
│   └── WEB-INF/
│       └── web.xml             # Servlet mappings
├── database/
│   └── schema.sql              # Database schema (reference)
├── pom.xml                     # Maven configuration
└── README.md                   # This file
```

## 🎨 Features Detail

### Enhanced Student Registration
- Department dropdown
- Year of study selection
- Phone number with validation
- Reason for attending (text area)
- Terms & conditions checkbox

### User Management for Organizers
- View all registrations grouped by event
- Export-friendly table format
- Student contact details visible
- Registration timestamps

### QR Code System
- Automatic generation per registered event
- 300x300 pixel high-quality QR codes
- Contains event name, venue, and date
- Works with any QR scanner app

## 💡 Tips for Beginners

1. **Ask GitHub Copilot for help**: In VS Code, press `Ctrl + I` and ask questions like:
   - "How do I add a new field to the registration form?"
   - "Explain this servlet code"
   - "How do I change the color scheme?"

2. **Testing**: Always test with all three roles (admin, organizer, student)

3. **Database**: Use MySQL Workbench to view and edit data visually

4. **Logs**: Check the terminal output for error messages

5. **CSS Changes**: Modify `web/css/style.css` to change colors and styling

## 📞 Need Help?

If you get stuck:
1. Check the Troubleshooting section above
2. Use GitHub Copilot in VS Code (`Ctrl + I`)
3. Check terminal output for error messages
4. Verify MySQL is running and database exists

## 🎉 You're All Set!

Enjoy managing your college events! The application is fully functional with:
- ✅ Role-based authentication
- ✅ Event creation and approval workflow
- ✅ Student registration with detailed forms
- ✅ QR code generation
- ✅ User management
- ✅ MySQL database integration

Happy coding! 🚀
