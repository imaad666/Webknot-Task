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

import com.webknot.campus.dto.RegistrationRequest;
import com.webknot.campus.dto.RegistrationResponse;
import com.webknot.campus.entity.Event;
import com.webknot.campus.entity.Registration;
import com.webknot.campus.entity.Student;
import com.webknot.campus.repository.EventRepository;
import com.webknot.campus.repository.RegistrationRepository;
import com.webknot.campus.repository.StudentRepository;
import com.webknot.campus.service.QRCodeService;

@RestController
@RequestMapping("/api/registrations")
@CrossOrigin(origins = "*")
public class RegistrationController {
    
    @Autowired
    private RegistrationRepository registrationRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private QRCodeService qrCodeService;
    
    /**
     * Register student for an event
     */
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> registerStudent(@RequestBody RegistrationRequest request) {
        try {
            // Validate student exists
            Optional<Student> studentOpt = studentRepository.findById(request.getStudentId());
            if (studentOpt.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            // Validate event exists
            Optional<Event> eventOpt = eventRepository.findById(request.getEventId());
            if (eventOpt.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            Student student = studentOpt.get();
            Event event = eventOpt.get();
            
            // Check if already registered
            if (registrationRepository.isStudentRegisteredForEvent(request.getStudentId(), request.getEventId())) {
                return ResponseEntity.badRequest().build();
            }
            
            // Check if event is full
            Long currentRegistrations = registrationRepository.countActiveRegistrationsByEvent(request.getEventId());
            if (currentRegistrations >= event.getCapacity()) {
                return ResponseEntity.badRequest().build();
            }
            
            // Create registration
            Registration registration = new Registration(student, event);
            registration = registrationRepository.save(registration);
            
            // Generate QR code
            String qrCode = qrCodeService.generateQRCode(registration.getQrToken());
            registration.setQrCode(qrCode);
            registration = registrationRepository.save(registration);
            
            // Create response
            RegistrationResponse response = new RegistrationResponse();
            response.setRegistrationId(registration.getRegistrationId());
            response.setStudentId(registration.getStudent().getStudentId());
            response.setEventId(registration.getEvent().getEventId());
            response.setQrCode(registration.getQrCode());
            response.setQrToken(registration.getQrToken());
            response.setStatus(registration.getStatus());
            response.setRegistrationDate(registration.getRegistrationDate());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get all registrations
     */
    @GetMapping
    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }
    
    /**
     * Get registrations by student
     */
    @GetMapping("/student/{studentId}")
    public List<Registration> getRegistrationsByStudent(@PathVariable String studentId) {
        return registrationRepository.findByStudentStudentId(studentId);
    }
    
    /**
     * Get registrations by event
     */
    @GetMapping("/event/{eventId}")
    public List<Registration> getRegistrationsByEvent(@PathVariable String eventId) {
        return registrationRepository.findByEventEventId(eventId);
    }
    
    /**
     * Get registration by QR token
     */
    @GetMapping("/qr/{qrToken}")
    public ResponseEntity<Registration> getRegistrationByQrToken(@PathVariable String qrToken) {
        Optional<Registration> registration = registrationRepository.findByQrToken(qrToken);
        return registration.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get registrations with attendance status for an event
     */
    @GetMapping("/event/{eventId}/with-attendance")
    public List<Registration> getRegistrationsWithAttendance(@PathVariable String eventId) {
        return registrationRepository.findRegistrationsWithAttendanceByEvent(eventId);
    }
}
