package com.webknot.campus.service;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import com.webknot.campus.entity.Event;
import com.webknot.campus.entity.Student;
import com.webknot.campus.repository.EventRepository;
import com.webknot.campus.repository.StudentRepository;

@Service
public class DataInitializationService implements CommandLineRunner {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private EventRepository eventRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Only initialize if database is empty
        if (studentRepository.count() == 0) {
            initializeData();
        }
    }
    
    private void initializeData() {
        System.out.println("Initializing sample data for WebKnotUni...");
        
        // Create sample students
        createSampleStudents();
        
        // Create sample events
        createSampleEvents();
        
        System.out.println("Sample data initialization completed!");
    }
    
    private void createSampleStudents() {
        Student[] students = {
            new Student("Aarav", "Sharma", "aarav.sharma@wku.edu", "9876543210", "CS", 2021, 1),
            new Student("Priya", "Patel", "priya.patel@wku.edu", "9876543211", "CS", 2021, 2),
            new Student("Rahul", "Kumar", "rahul.kumar@wku.edu", "9876543212", "IT", 2021, 1),
            new Student("Sneha", "Reddy", "sneha.reddy@wku.edu", "9876543213", "CS", 2022, 1),
            new Student("Arjun", "Singh", "arjun.singh@wku.edu", "9876543214", "IT", 2022, 1),
            new Student("Maya", "Gupta", "maya.gupta@wku.edu", "9876543215", "CS", 2020, 1),
            new Student("Vikram", "Joshi", "vikram.joshi@wku.edu", "9876543216", "ME", 2021, 1),
            new Student("Kavya", "Nair", "kavya.nair@wku.edu", "9876543217", "ME", 2022, 1),
            new Student("Rohan", "Verma", "rohan.verma@wku.edu", "9876543218", "CS", 2023, 1),
            new Student("Anita", "Shah", "anita.shah@wku.edu", "9876543219", "IT", 2023, 1)
        };
        
        for (Student student : students) {
            studentRepository.save(student);
            System.out.println("Created student: " + student.getStudentId() + " - " + student.getFullName());
        }
    }
    
    private void createSampleEvents() {
        Event[] events = {
            new Event(
                "AI & Machine Learning Workshop", 
                "Hands-on workshop covering fundamentals of AI and ML with practical coding sessions and real-world applications",
                "Workshop",
                LocalDate.of(2025, 9, 15),
                LocalTime.of(10, 0),
                "Computer Lab 1",
                50,
                "Dr. Rajesh Kumar",
                "rajesh.kumar@wku.edu"
            ),
            new Event(
                "TechnoFest 2025", 
                "Annual technology festival featuring competitions, exhibitions, networking opportunities, and industry showcases",
                "Fest",
                LocalDate.of(2025, 9, 20),
                LocalTime.of(9, 0),
                "Main Auditorium",
                200,
                "Prof. Anita Sharma",
                "anita.sharma@wku.edu"
            ),
            new Event(
                "Career Development Seminar", 
                "Industry experts share insights on career planning, professional development, and job market trends",
                "Seminar",
                LocalDate.of(2025, 9, 18),
                LocalTime.of(14, 0),
                "Seminar Hall",
                100,
                "Dr. Pradeep Singh",
                "pradeep.singh@wku.edu"
            ),
            new Event(
                "Web Development Bootcamp", 
                "3-day intensive bootcamp covering modern web development technologies, frameworks, and deployment strategies",
                "Workshop",
                LocalDate.of(2025, 9, 25),
                LocalTime.of(9, 0),
                "Computer Lab 2",
                30,
                "Mr. Karthik Rao",
                "karthik.rao@wku.edu"
            ),
            new Event(
                "Innovation Summit", 
                "Showcase of student innovations, startup ideas, research presentations, and entrepreneurship opportunities",
                "Fest",
                LocalDate.of(2025, 9, 28),
                LocalTime.of(11, 0),
                "Innovation Center",
                150,
                "Dr. Meera Jain",
                "meera.jain@wku.edu"
            ),
            new Event(
                "Cybersecurity Awareness Workshop", 
                "Learn about cybersecurity threats, protection strategies, and best practices for digital safety",
                "Workshop",
                LocalDate.of(2025, 10, 5),
                LocalTime.of(15, 0),
                "Computer Lab 3",
                40,
                "Mr. Sameer Agarwal",
                "sameer.agarwal@wku.edu"
            ),
            new Event(
                "Industry Connect Seminar", 
                "Connect with industry professionals, learn about current trends, and explore internship opportunities",
                "Seminar",
                LocalDate.of(2025, 10, 10),
                LocalTime.of(10, 30),
                "Conference Room",
                80,
                "Ms. Priyanka Mishra",
                "priyanka.mishra@wku.edu"
            )
        };
        
        for (Event event : events) {
            eventRepository.save(event);
            System.out.println("Created event: " + event.getEventId() + " - " + event.getEventName());
        }
    }
}
