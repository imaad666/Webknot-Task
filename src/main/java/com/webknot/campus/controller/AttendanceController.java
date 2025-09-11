package com.webknot.campus.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webknot.campus.dto.AttendanceRequest;
import com.webknot.campus.dto.AttendanceResponse;
import com.webknot.campus.entity.Attendance;
import com.webknot.campus.entity.Registration;
import com.webknot.campus.repository.AttendanceRepository;
import com.webknot.campus.repository.RegistrationRepository;
import com.webknot.campus.service.QRCodeService;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {
    
    @Autowired
    private AttendanceRepository attendanceRepository;
    
    @Autowired
    private RegistrationRepository registrationRepository;
    
    @Autowired
    private QRCodeService qrCodeService;
    
    /**
     * Mark attendance via QR code scan
     */
    @PostMapping("/qr-scan")
    public ResponseEntity<AttendanceResponse> markAttendanceByQR(@RequestBody AttendanceRequest request) {
        try {
            // Validate QR token format
            if (!qrCodeService.isValidQRToken(request.getQrToken())) {
                return ResponseEntity.badRequest().build();
            }
            
            // Find registration by QR token
            Optional<Registration> registrationOpt = registrationRepository.findByQrToken(request.getQrToken());
            if (registrationOpt.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            Registration registration = registrationOpt.get();
            
            // Check if already attended
            if (attendanceRepository.findByRegistrationRegistrationId(registration.getRegistrationId()).isPresent()) {
                return ResponseEntity.badRequest().build();
            }
            
            // Check if registration is active
            if (!"REGISTERED".equals(registration.getStatus())) {
                return ResponseEntity.badRequest().build();
            }
            
            // Create attendance record
            Attendance attendance = new Attendance(registration, "QR_SCAN", request.getScannedBy());
            attendance = attendanceRepository.save(attendance);
            
            // Create response
            AttendanceResponse response = new AttendanceResponse();
            response.setAttendanceId(attendance.getAttendanceId());
            response.setStudentId(attendance.getStudent().getStudentId());
            response.setStudentName(attendance.getStudent().getFullName());
            response.setEventId(attendance.getEvent().getEventId());
            response.setEventName(attendance.getEvent().getEventName());
            response.setCheckInTime(attendance.getCheckInTime());
            response.setCheckInMethod(attendance.getCheckInMethod());
            response.setScannedBy(attendance.getScannedBy());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Mark attendance manually
     */
    @PostMapping("/manual")
    public ResponseEntity<AttendanceResponse> markAttendanceManually(@RequestBody AttendanceRequest request) {
        try {
            // Find registration
            Optional<Registration> registrationOpt = registrationRepository
                .findByStudentStudentIdAndEventEventId(request.getStudentId(), request.getEventId());
                
            if (registrationOpt.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            Registration registration = registrationOpt.get();
            
            // Check if already attended
            if (attendanceRepository.findByRegistrationRegistrationId(registration.getRegistrationId()).isPresent()) {
                return ResponseEntity.badRequest().build();
            }
            
            // Create attendance record
            Attendance attendance = new Attendance(registration, "MANUAL", request.getScannedBy());
            attendance = attendanceRepository.save(attendance);
            
            // Create response
            AttendanceResponse response = new AttendanceResponse();
            response.setAttendanceId(attendance.getAttendanceId());
            response.setStudentId(attendance.getStudent().getStudentId());
            response.setStudentName(attendance.getStudent().getFullName());
            response.setEventId(attendance.getEvent().getEventId());
            response.setEventName(attendance.getEvent().getEventName());
            response.setCheckInTime(attendance.getCheckInTime());
            response.setCheckInMethod(attendance.getCheckInMethod());
            response.setScannedBy(attendance.getScannedBy());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get all attendance records
     */
    @GetMapping
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }
    
    /**
     * Get attendance by event
     */
    @GetMapping("/event/{eventId}")
    public List<Attendance> getAttendanceByEvent(@PathVariable String eventId) {
        return attendanceRepository.findByEventEventId(eventId);
    }
    
    /**
     * Get attendance by student
     */
    @GetMapping("/student/{studentId}")
    public List<Attendance> getAttendanceByStudent(@PathVariable String studentId) {
        return attendanceRepository.findByStudentStudentId(studentId);
    }
    
    /**
     * Check if student attended specific event
     */
    @GetMapping("/check/{studentId}/{eventId}")
    public ResponseEntity<Boolean> checkStudentAttendance(@PathVariable String studentId, @PathVariable String eventId) {
        boolean attended = attendanceRepository.hasStudentAttendedEvent(studentId, eventId);
        return ResponseEntity.ok(attended);
    }
    
    /**
     * Get attendance count by event
     */
    @GetMapping("/count/event/{eventId}")
    public ResponseEntity<Long> getAttendanceCountByEvent(@PathVariable String eventId) {
        Long count = attendanceRepository.countAttendanceByEvent(eventId);
        return ResponseEntity.ok(count);
    }
}
