package com.webknot.campus.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webknot.campus.repository.AttendanceRepository;
import com.webknot.campus.repository.EventRepository;
import com.webknot.campus.repository.FeedbackRepository;
import com.webknot.campus.repository.RegistrationRepository;
import com.webknot.campus.repository.StudentRepository;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportsController {
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private RegistrationRepository registrationRepository;
    
    @Autowired
    private AttendanceRepository attendanceRepository;
    
    @Autowired
    private FeedbackRepository feedbackRepository;
    
    /**
     * Event Popularity Report - Sorted by number of registrations
     */
    @GetMapping("/event-popularity")
    public List<Map<String, Object>> getEventPopularityReport() {
        List<Object[]> results = eventRepository.getEventRegistrationCounts();
        List<Map<String, Object>> report = new ArrayList<>();
        
        for (Object[] result : results) {
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("eventId", result[0]);
            eventData.put("eventName", result[1]);
            eventData.put("registrationCount", result[2]);
            report.add(eventData);
        }
        
        return report;
    }
    
    /**
     * Event Popularity Report by Type
     */
    @GetMapping("/event-popularity/type/{eventType}")
    public List<Map<String, Object>> getEventPopularityByType(@PathVariable String eventType) {
        List<com.webknot.campus.entity.Event> events = eventRepository.findEventsByTypeOrderByPopularity(eventType);
        List<Map<String, Object>> report = new ArrayList<>();
        
        // Need to get registration counts for these events
        for (com.webknot.campus.entity.Event event : events) {
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("eventId", event.getEventId());
            eventData.put("eventName", event.getEventName());
            eventData.put("eventType", event.getEventType());
            eventData.put("registrationCount", event.getCurrentRegistrationCount());
            report.add(eventData);
        }
        
        return report;
    }
    
    /**
     * Student Participation Report - How many events a student attended
     */
    @GetMapping("/student-participation")
    public List<Map<String, Object>> getStudentParticipationReport() {
        List<Object[]> results = registrationRepository.getStudentParticipationReport();
        List<Map<String, Object>> report = new ArrayList<>();
        
        for (Object[] result : results) {
            Map<String, Object> studentData = new HashMap<>();
            studentData.put("studentId", result[0]);
            studentData.put("firstName", result[1]);
            studentData.put("lastName", result[2]);
            studentData.put("fullName", result[1] + " " + result[2]);
            studentData.put("totalRegistrations", result[3]);
            studentData.put("totalAttendance", result[4]);
            
            // Calculate attendance percentage
            Long registrations = (Long) result[3];
            Long attendance = (Long) result[4];
            Double attendancePercentage = (registrations > 0) ? 
                (attendance.doubleValue() / registrations.doubleValue() * 100) : 0.0;
            studentData.put("attendancePercentage", Math.round(attendancePercentage * 100.0) / 100.0);
            
            report.add(studentData);
        }
        
        return report;
    }
    
    /**
     * Top 3 Most Active Students (Bonus)
     */
    @GetMapping("/top-active-students")
    public List<Map<String, Object>> getTopActiveStudents(@RequestParam(defaultValue = "3") int limit) {
        List<Object[]> results = attendanceRepository.getMostActiveStudentsByAttendance();
        List<Map<String, Object>> report = new ArrayList<>();
        
        int count = 0;
        for (Object[] result : results) {
            if (count >= limit) break;
            
            Map<String, Object> studentData = new HashMap<>();
            studentData.put("studentId", result[0]);
            studentData.put("firstName", result[1]);
            studentData.put("lastName", result[2]);
            studentData.put("fullName", result[1] + " " + result[2]);
            studentData.put("attendanceCount", result[3]);
            studentData.put("rank", count + 1);
            
            report.add(studentData);
            count++;
        }
        
        return report;
    }
    
    /**
     * Attendance Percentage Report by Event
     */
    @GetMapping("/attendance-percentage")
    public List<Map<String, Object>> getAttendancePercentageReport() {
        List<Object[]> results = eventRepository.getEventAttendanceStats();
        List<Map<String, Object>> report = new ArrayList<>();
        
        for (Object[] result : results) {
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("eventId", result[0]);
            eventData.put("eventName", result[1]);
            eventData.put("totalRegistrations", result[2]);
            eventData.put("totalAttendance", result[3]);
            eventData.put("attendancePercentage", 
                Math.round(((Double) result[4]) * 100.0) / 100.0);
            report.add(eventData);
        }
        
        return report;
    }
    
    /**
     * Average Feedback Score Report
     */
    @GetMapping("/feedback-scores")
    public List<Map<String, Object>> getFeedbackScoresReport() {
        List<Object[]> results = eventRepository.getEventFeedbackStats();
        List<Map<String, Object>> report = new ArrayList<>();
        
        for (Object[] result : results) {
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("eventId", result[0]);
            eventData.put("eventName", result[1]);
            
            Double avgRating = (Double) result[2];
            eventData.put("averageRating", 
                avgRating != null ? Math.round(avgRating * 100.0) / 100.0 : 0.0);
            eventData.put("feedbackCount", result[3]);
            report.add(eventData);
        }
        
        return report;
    }
    
    /**
     * Comprehensive Dashboard Report
     */
    @GetMapping("/dashboard")
    public Map<String, Object> getDashboardReport() {
        Map<String, Object> dashboard = new HashMap<>();
        
        // Basic counts
        dashboard.put("totalEvents", eventRepository.count());
        dashboard.put("totalStudents", studentRepository.count());
        dashboard.put("totalRegistrations", registrationRepository.count());
        dashboard.put("totalAttendance", attendanceRepository.count());
        dashboard.put("totalFeedback", feedbackRepository.count());
        
        // Average statistics
        Object[] feedbackStats = feedbackRepository.getFeedbackStatsSummary();
        if (feedbackStats != null && feedbackStats.length >= 6) {
            Double avgRating = (Double) feedbackStats[1];
            dashboard.put("averageFeedbackScore", 
                avgRating != null ? Math.round(avgRating * 100.0) / 100.0 : 0.0);
            dashboard.put("positiveFeedbackCount", feedbackStats[4] != null ? feedbackStats[4] : 0);
            dashboard.put("negativeFeedbackCount", feedbackStats[5] != null ? feedbackStats[5] : 0);
        } else {
            dashboard.put("averageFeedbackScore", 0.0);
            dashboard.put("positiveFeedbackCount", 0);
            dashboard.put("negativeFeedbackCount", 0);
        }
        
        // Calculate overall attendance rate
        long totalReg = registrationRepository.count();
        long totalAtt = attendanceRepository.count();
        double overallAttendanceRate = (totalReg > 0) ? 
            (totalAtt * 100.0 / totalReg) : 0.0;
        dashboard.put("overallAttendanceRate", 
            Math.round(overallAttendanceRate * 100.0) / 100.0);
        
        return dashboard;
    }
    
    /**
     * Event Type Statistics (Bonus)
     */
    @GetMapping("/event-type-stats")
    public List<Map<String, Object>> getEventTypeStatistics() {
        // Get events by each type
        String[] eventTypes = {"Workshop", "Fest", "Seminar"};
        List<Map<String, Object>> report = new ArrayList<>();
        
        for (String eventType : eventTypes) {
            Map<String, Object> typeData = new HashMap<>();
            typeData.put("eventType", eventType);
            
            List<com.webknot.campus.entity.Event> events = eventRepository.findByEventType(eventType);
            typeData.put("eventCount", events.size());
            
            // Calculate total registrations for this type
            int totalRegistrations = events.stream()
                .mapToInt(com.webknot.campus.entity.Event::getCurrentRegistrationCount)
                .sum();
            typeData.put("totalRegistrations", totalRegistrations);
            
            // Get feedback stats for this type
            List<Object[]> feedbackStats = feedbackRepository.getAverageRatingByEventType();
            for (Object[] stat : feedbackStats) {
                if (eventType.equals(stat[0])) {
                    typeData.put("averageRating", 
                        Math.round(((Double) stat[1]) * 100.0) / 100.0);
                    typeData.put("feedbackCount", stat[2]);
                    break;
                }
            }
            
            report.add(typeData);
        }
        
        return report;
    }
}
