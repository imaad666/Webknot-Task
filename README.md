# Campus Event Management System - My Coding Journey

this is my submission for the webknot campus drive assignment.

## What I Built 🏗️

A campus event management system where:
- Admins can create events and see who's coming
- Students can register for events and get QR codes for attendance
- There's reports on metrics for analytics

**Tech Stack:** Java Spring Boot, SQLite, HTML/CSS/JS 

## My Step-by-Step Journey 

### Step 1: Reading the Assignment
- Got the PDF assignment about building event management system
- Realized I needed both admin portal and student app
- Decided to make it web-based instead of separate mobile app

### Step 2: Planning Phase
- Chose Java Spring Boot since I know SB well
- Planned for REST APIs 
- Decided on unique event ID format: `WKUe<DDMMYY>` (e.g., `WKUe120925`) [fyi WKU is WebKnotUni]
- Student ID format: `1WKU<year><course><roll>` (e.g., `1WKU21CS001`)
- Added QR code idea for attendance

### Step 3: Basic Setup
- Created simple landing page with "Admin" and "Student" buttons
- Made separate pages for admins and students
- Set up Spring Boot project with basic structure

### Step 4: Database & Entities
- Started with H2 in-memory database (mistake #1)
- Created entities: Student, Event, Registration, Attendance, Feedback
- Set up JPA relationships

### Step 5: Building APIs
- Created controllers for all CRUD operations
- Added QR code generation using ZXing library
- Made registration system with unique QR tokens
- Added all the required reports endpoints

### Step 6: Frontend Development  
- Built admin portal with tabs for different functions
- Created student portal with event browsing
- Added forms for event creation and student registration
- Made it look decent with CSS (tried my best)

### Step 7: First Major Crisis - Nothing Works 💀

**Problems I found:**
- Event creation was broken
- Teachers could add students (which was dumb)
- UI looked terrible
- Data wasn't persisting

**Fixes:**
- Removed teacher ability to add students
- Fixed event creation bugs
- Switched to SQLite for persistent storage

### Step 8: Security Wake-up Call 🚨

**Security fixes added:**
- Admin login page with authentication
- Session management (12-hour sessions)
- Logout functionality
- Access control checks

### Step 9: Student Security Issues

**More security fixes:**
- Student login system
- Student can only see their own registrations
- No manual student ID entry allowed
- Session-based authentication for students too

### Step 10: Database Issues

**Database migration:**
- Switched from H2 to SQLite for development
- Added proper dependencies to pom.xml
- Updated application.yml configuration
- Now data actually persists between restarts

### Step 11: Event Deletion Problems

**Fix:** Added missing @DeleteMapping annotation to EventController

### Step 12: Event Creation Still Broken
Even after all fixes, event creation was giving 400 errors.

**Root causes found:**
1. **Circular JSON references** - Events had Registrations, Registrations had Events = infinite loop
2. **Duplicate event IDs** - Multiple events on same date would conflict
3. **Field mapping issues** - Frontend sending `location` but backend expecting `venue`

**Fixes:**
- Added `@JsonIgnore` to prevent circular references
- Enhanced event ID generation with timestamps for uniqueness
- Fixed frontend form to send correct field names
- Split datetime input into separate date and time fields

### Step 13: Registration Count Crisis

**The problem:** 
- Event entity had `@JsonIgnore` on registrations field
- No method to calculate registration count
- Frontend couldn't display counts

**The solution:**
- Added `getCurrentRegistrationCount()` method to Event entity
- Modified EventController to include live registration counts
- Used repository queries to get accurate counts
- Now counts update in real-time when students register

## Major Bugs I Fixed

1. **Circular JSON serialization** - Added @JsonIgnore annotations
2. **Event ID conflicts** - Enhanced ID generation with timestamps  
3. **Missing authentication** - Added login systems for both admin and students
4. **Database persistence** - Switched from H2 to SQLite
5. **Field mapping errors** - Fixed frontend-backend communication
6. **Registration count updates** - Implemented real-time count calculation
7. **Security vulnerabilities** - Prevented stalking and fake reviews
8. **UI issues** - Fixed scrolling and responsive design

## Current Features

### Admin Portal
- ✅ Create, view, delete events
- ✅ Dashboard with statistics  
- ✅ Real-time registration counts
- ✅ QR code attendance scanning
- ✅ Multiple reports (popularity, participation, etc.)
- ✅ Secure authentication

### Student Portal  
- ✅ Browse and register for events
- ✅ View personal registrations with QR tickets
- ✅ Submit feedback/ratings
- ✅ Secure login (can't see other students' data)

### Backend APIs
- ✅ RESTful endpoints for all operations
- ✅ QR code generation and scanning
- ✅ Registration management
- ✅ Attendance tracking  
- ✅ Feedback collection
- ✅ Analytics and reporting

## Database Schema 📊

```sql
Students: student_id, name, email, course, year, etc.
Events: event_id, name, type, date, venue, capacity, etc.
Registrations: registration_id, student_id, event_id, qr_code, status
Attendance: attendance_id, registration_id, check_in_time
Feedback: feedback_id, registration_id, rating, comments
```

## How to Run This Thing 🚀

### Prerequisites (you need these installed)
- Java 17 or higher (check with `java -version`)
- Maven (check with `mvn -version`) 
- Git (to clone the repo)

### Setup Steps
1. **Clone the repository:**
   ```bash
   git clone <your-repo-url>
   cd Webknot-Task
   ```

2. **Install dependencies:**
   ```bash
   mvn clean install
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```
   
4. **Wait for it to start** (takes like 30 seconds)
   - You'll see logs saying "Started CampusEventManagementApplication"
   - Database gets created automatically (SQLite file)

5. **Open your browser and go to:**
   ```
   http://localhost:8080
   ```

6. **Login and test:**
   - Click "Admin Portal" → Use any username/password (demo mode)
   - Click "Student Access" → You'll need to register first or use existing student IDs
   - Sample student ID: `1WKU21CS001` (if it exists)

### What You'll See
- Landing page with Admin and Student buttons
- Admin portal: Create events, view dashboard, generate reports
- Student portal: Browse events, register, get QR tickets
- Everything persists in a SQLite database file

### Troubleshooting 
- **Port 8080 already in use?** Kill other processes or change port in `application.yml`
- **Maven not found?** Install it or use the wrapper: `./mvnw spring-boot:run` (if available)
- **Java version issues?** Make sure you have Java 17+
- **Database errors?** Delete the `wku_campus_events.db` file and restart

## API Endpoints 📡

- `GET/POST /api/events` - Event management
- `POST /api/registrations/register` - Student registration  
- `POST /api/attendance/manual` - Mark attendance
- `POST /api/feedback` - Submit feedback
- `GET /api/reports/*` - Various analytics reports


*Made with determination, Stack Overflow, and way too much debugging by a CS student who just wanted to pass the assignment lol*
