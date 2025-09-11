package com.webknot.campus.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webknot.campus.entity.Attendance;
import com.webknot.campus.entity.Event;
import com.webknot.campus.entity.Feedback;
import com.webknot.campus.entity.Registration;
import com.webknot.campus.entity.Student;
import com.webknot.campus.repository.AttendanceRepository;
import com.webknot.campus.repository.EventRepository;
import com.webknot.campus.repository.FeedbackRepository;
import com.webknot.campus.repository.RegistrationRepository;
import com.webknot.campus.repository.StudentRepository;
import com.webknot.campus.service.QRCodeService;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private RegistrationRepository registrationRepository;
    
    @Autowired
    private AttendanceRepository attendanceRepository;
    
    @Autowired
    private FeedbackRepository feedbackRepository;
    
    @Autowired
    private QRCodeService qrCodeService;
    
    /**
     * Create test registrations with QR codes
     */
    @PostMapping("/create-sample-data")
    public ResponseEntity<Map<String, Object>> createSampleData() {
        try {
            Map<String, Object> result = new HashMap<>();
            List<String> actions = new ArrayList<>();
            
            // Get some students and events
            List<Student> students = studentRepository.findAll();
            List<Event> events = eventRepository.findAll();
            
            if (students.isEmpty() || events.isEmpty()) {
                result.put("error", "No students or events found");
                return ResponseEntity.badRequest().body(result);
            }
            
            // Create sample registrations
            int registrationCount = 0;
            for (int i = 0; i < Math.min(5, students.size()); i++) {
                for (int j = 0; j < Math.min(3, events.size()); j++) {
                    Student student = students.get(i);
                    Event event = events.get(j);
                    
                    // Check if already registered
                    Optional<Registration> existing = registrationRepository
                        .findByStudentStudentIdAndEventEventId(student.getStudentId(), event.getEventId());
                    
                    if (existing.isEmpty()) {
                        Registration registration = new Registration(student, event);
                        registration = registrationRepository.save(registration);
                        
                        // Generate QR code
                        String qrCode = qrCodeService.generateQRCode(registration.getQrToken());
                        registration.setQrCode(qrCode);
                        registrationRepository.save(registration);
                        
                        actions.add("Registered " + student.getStudentId() + " for " + event.getEventId());
                        registrationCount++;
                    }
                }
            }
            
            // Create some attendance records
            List<Registration> registrations = registrationRepository.findAll();
            int attendanceCount = 0;
            for (int i = 0; i < Math.min(3, registrations.size()); i++) {
                Registration registration = registrations.get(i);
                
                // Check if already attended
                if (attendanceRepository.findByRegistrationRegistrationId(registration.getRegistrationId()).isEmpty()) {
                    Attendance attendance = new Attendance(registration, "QR_SCAN", "admin@wku.edu");
                    attendanceRepository.save(attendance);
                    actions.add("Marked attendance for " + registration.getStudent().getStudentId() + 
                               " at " + registration.getEvent().getEventId());
                    attendanceCount++;
                }
            }
            
            // Create some feedback
            int feedbackCount = 0;
            for (int i = 0; i < Math.min(2, registrations.size()); i++) {
                Registration registration = registrations.get(i);
                
                // Check if feedback already exists
                if (feedbackRepository.findByRegistrationRegistrationId(registration.getRegistrationId()).isEmpty()) {
                    int rating = (i % 2 == 0) ? 5 : 4; // Alternate between 5 and 4 star ratings
                    String comment = rating == 5 ? "Excellent event! Very informative." : "Good event, learned a lot.";
                    
                    Feedback feedback = new Feedback(registration, rating, comment);
                    feedbackRepository.save(feedback);
                    actions.add("Added feedback from " + registration.getStudent().getStudentId() + 
                               " for " + registration.getEvent().getEventId() + " (Rating: " + rating + ")");
                    feedbackCount++;
                }
            }
            
            result.put("success", true);
            result.put("registrationsCreated", registrationCount);
            result.put("attendanceRecordsCreated", attendanceCount);
            result.put("feedbackRecordsCreated", feedbackCount);
            result.put("actions", actions);
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Get system status
     */
    @GetMapping("/status")
    public Map<String, Object> getSystemStatus() {
        Map<String, Object> status = new HashMap<>();
        
        status.put("totalStudents", studentRepository.count());
        status.put("totalEvents", eventRepository.count());
        status.put("totalRegistrations", registrationRepository.count());
        status.put("totalAttendance", attendanceRepository.count());
        status.put("totalFeedback", feedbackRepository.count());
        
        return status;
    }
    
    /**
     * Test QR code functionality
     */
    @GetMapping("/qr-test")
    public Map<String, Object> testQRCode() {
        Map<String, Object> result = new HashMap<>();
        
        String testToken = "WKUe150925_1WKU21CS001_1694444400";
        result.put("testToken", testToken);
        result.put("isValid", qrCodeService.isValidQRToken(testToken));
        result.put("eventId", qrCodeService.getEventIdFromToken(testToken));
        result.put("studentId", qrCodeService.getStudentIdFromToken(testToken));
        result.put("timestamp", qrCodeService.getTimestampFromToken(testToken));
        result.put("qrContent", qrCodeService.getQRCodeContent(testToken));
        
        return result;
    }
}
