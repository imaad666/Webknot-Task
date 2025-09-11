-- WebKnotUni Campus Event Management Database Schema
-- College: WebKnotUni (WKU)
-- Student ID Format: 1WKU<year><course><roll> (e.g., 1WKU21CS001)
-- Event ID Format: WKUe<date> (e.g., WKUe120225)

-- Students table
CREATE TABLE students (
    student_id VARCHAR(20) PRIMARY KEY, -- 1WKU21CS001
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(15),
    course VARCHAR(10) NOT NULL, -- CS, IT, ME, etc.
    year_of_joining INTEGER NOT NULL, -- 2021, 2022, etc.
    roll_number INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Events table
CREATE TABLE events (
    event_id VARCHAR(20) PRIMARY KEY, -- WKUe120225
    event_name VARCHAR(255) NOT NULL,
    description TEXT,
    event_type VARCHAR(20) NOT NULL, -- Workshop, Fest, Seminar
    event_date DATE NOT NULL,
    event_time TIME NOT NULL,
    venue VARCHAR(255) NOT NULL,
    capacity INTEGER NOT NULL,
    organizer_name VARCHAR(100),
    organizer_email VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Event registrations table
CREATE TABLE registrations (
    registration_id SERIAL PRIMARY KEY,
    student_id VARCHAR(20) NOT NULL,
    event_id VARCHAR(20) NOT NULL,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    qr_code VARCHAR(500) NOT NULL UNIQUE, -- Base64 encoded QR code data
    qr_token VARCHAR(100) NOT NULL UNIQUE, -- Unique token for QR scanning
    status VARCHAR(20) DEFAULT 'REGISTERED', -- REGISTERED, CANCELLED
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (event_id) REFERENCES events(event_id),
    UNIQUE(student_id, event_id) -- Prevent duplicate registrations
);

-- Attendance table
CREATE TABLE attendance (
    attendance_id SERIAL PRIMARY KEY,
    registration_id INTEGER NOT NULL,
    student_id VARCHAR(20) NOT NULL,
    event_id VARCHAR(20) NOT NULL,
    check_in_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    check_in_method VARCHAR(20) DEFAULT 'QR_SCAN', -- QR_SCAN, MANUAL
    scanned_by VARCHAR(100), -- Admin who scanned (optional)
    FOREIGN KEY (registration_id) REFERENCES registrations(registration_id),
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (event_id) REFERENCES events(event_id),
    UNIQUE(registration_id) -- One attendance per registration
);

-- Feedback table
CREATE TABLE feedback (
    feedback_id SERIAL PRIMARY KEY,
    registration_id INTEGER NOT NULL,
    student_id VARCHAR(20) NOT NULL,
    event_id VARCHAR(20) NOT NULL,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comments TEXT,
    feedback_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (registration_id) REFERENCES registrations(registration_id),
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (event_id) REFERENCES events(event_id),
    UNIQUE(registration_id) -- One feedback per registration
);

-- Create indexes for better performance
CREATE INDEX idx_students_course_year ON students(course, year_of_joining);
CREATE INDEX idx_events_date_type ON events(event_date, event_type);
CREATE INDEX idx_registrations_event ON registrations(event_id);
CREATE INDEX idx_registrations_student ON registrations(student_id);
CREATE INDEX idx_attendance_event ON attendance(event_id);
CREATE INDEX idx_feedback_event ON feedback(event_id);

-- Insert sample data for WebKnotUni
INSERT INTO students (student_id, first_name, last_name, email, phone, course, year_of_joining, roll_number) VALUES
('1WKU21CS001', 'Aarav', 'Sharma', 'aarav.sharma@wku.edu', '9876543210', 'CS', 2021, 1),
('1WKU21CS002', 'Priya', 'Patel', 'priya.patel@wku.edu', '9876543211', 'CS', 2021, 2),
('1WKU21IT001', 'Rahul', 'Kumar', 'rahul.kumar@wku.edu', '9876543212', 'IT', 2021, 1),
('1WKU22CS001', 'Sneha', 'Reddy', 'sneha.reddy@wku.edu', '9876543213', 'CS', 2022, 1),
('1WKU22IT001', 'Arjun', 'Singh', 'arjun.singh@wku.edu', '9876543214', 'IT', 2022, 1),
('1WKU20CS001', 'Maya', 'Gupta', 'maya.gupta@wku.edu', '9876543215', 'CS', 2020, 1),
('1WKU21ME001', 'Vikram', 'Joshi', 'vikram.joshi@wku.edu', '9876543216', 'ME', 2021, 1),
('1WKU22ME001', 'Kavya', 'Nair', 'kavya.nair@wku.edu', '9876543217', 'ME', 2022, 1);

INSERT INTO events (event_id, event_name, description, event_type, event_date, event_time, venue, capacity, organizer_name, organizer_email) VALUES
('WKUe120925', 'AI & Machine Learning Workshop', 'Hands-on workshop covering fundamentals of AI and ML with practical coding sessions', 'Workshop', '2025-09-12', '10:00:00', 'Computer Lab 1', 50, 'Dr. Rajesh Kumar', 'rajesh.kumar@wku.edu'),
('WKUe150925', 'TechnoFest 2025', 'Annual technology festival featuring competitions, exhibitions, and networking', 'Fest', '2025-09-15', '09:00:00', 'Main Auditorium', 200, 'Prof. Anita Sharma', 'anita.sharma@wku.edu'),
('WKUe180925', 'Career Development Seminar', 'Industry experts share insights on career planning and professional development', 'Seminar', '2025-09-18', '14:00:00', 'Seminar Hall', 100, 'Dr. Pradeep Singh', 'pradeep.singh@wku.edu'),
('WKUe220925', 'Web Development Bootcamp', '3-day intensive bootcamp covering modern web development technologies', 'Workshop', '2025-09-22', '09:00:00', 'Computer Lab 2', 30, 'Mr. Karthik Rao', 'karthik.rao@wku.edu'),
('WKUe250925', 'Innovation Summit', 'Showcase of student innovations and startup ideas', 'Fest', '2025-09-25', '11:00:00', 'Innovation Center', 150, 'Dr. Meera Jain', 'meera.jain@wku.edu');

-- Sample registrations (with placeholder QR codes - will be generated by backend)
INSERT INTO registrations (student_id, event_id, qr_code, qr_token, status) VALUES
('1WKU21CS001', 'WKUe120925', 'qr_placeholder_1', 'WKUe120925_1WKU21CS001_' || EXTRACT(EPOCH FROM NOW()), 'REGISTERED'),
('1WKU21CS002', 'WKUe120925', 'qr_placeholder_2', 'WKUe120925_1WKU21CS002_' || EXTRACT(EPOCH FROM NOW()), 'REGISTERED'),
('1WKU21IT001', 'WKUe150925', 'qr_placeholder_3', 'WKUe150925_1WKU21IT001_' || EXTRACT(EPOCH FROM NOW()), 'REGISTERED'),
('1WKU22CS001', 'WKUe150925', 'qr_placeholder_4', 'WKUe150925_1WKU22CS001_' || EXTRACT(EPOCH FROM NOW()), 'REGISTERED'),
('1WKU21CS001', 'WKUe150925', 'qr_placeholder_5', 'WKUe150925_1WKU21CS001_' || EXTRACT(EPOCH FROM NOW()), 'REGISTERED'),
('1WKU20CS001', 'WKUe180925', 'qr_placeholder_6', 'WKUe180925_1WKU20CS001_' || EXTRACT(EPOCH FROM NOW()), 'REGISTERED');

-- Sample attendance (some students attended)
INSERT INTO attendance (registration_id, student_id, event_id, check_in_method, scanned_by) VALUES
(1, '1WKU21CS001', 'WKUe120925', 'QR_SCAN', 'admin@wku.edu'),
(2, '1WKU21CS002', 'WKUe120925', 'QR_SCAN', 'admin@wku.edu');

-- Sample feedback
INSERT INTO feedback (registration_id, student_id, event_id, rating, comments) VALUES
(1, '1WKU21CS001', 'WKUe120925', 5, 'Excellent workshop! Very informative and hands-on.'),
(2, '1WKU21CS002', 'WKUe120925', 4, 'Good content, but could use more practical examples.');
