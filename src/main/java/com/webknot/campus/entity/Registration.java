package com.webknot.campus.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "registrations", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "event_id"}))
public class Registration {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registration_id")
    private Long registrationId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false)
    @JsonIgnoreProperties({"registrations"})
    private Student student;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "event_id", nullable = false)
    @JsonIgnoreProperties({"registrations"})
    private Event event;
    
    @Column(name = "registration_date")
    private LocalDateTime registrationDate;
    
    @Column(name = "qr_code", length = 2000)
    private String qrCode; // Base64 encoded QR code
    
    @Column(name = "qr_token", length = 100, unique = true, nullable = false)
    private String qrToken; // Unique token for QR scanning
    
    @Column(name = "status", length = 20)
    @Pattern(regexp = "^(REGISTERED|CANCELLED)$", message = "Status must be REGISTERED or CANCELLED")
    private String status = "REGISTERED";
    
    @OneToOne(mappedBy = "registration", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Attendance attendance;
    
    @OneToOne(mappedBy = "registration", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Feedback feedback;
    
    // Constructors
    public Registration() {
        this.registrationDate = LocalDateTime.now();
        this.status = "REGISTERED";
    }
    
    public Registration(Student student, Event event) {
        this();
        this.student = student;
        this.event = event;
        this.qrToken = generateQrToken();
    }
    
    @PrePersist
    protected void onCreate() {
        if (registrationDate == null) {
            this.registrationDate = LocalDateTime.now();
        }
        if (qrToken == null) {
            this.qrToken = generateQrToken();
        }
    }
    
    private String generateQrToken() {
        // Format: eventId_studentId_timestamp
        // Example: WKUe120925_1WKU21CS001_1672531200
        long timestamp = System.currentTimeMillis() / 1000;
        return event.getEventId() + "_" + student.getStudentId() + "_" + timestamp;
    }
    
    // Getters and Setters
    public Long getRegistrationId() { return registrationId; }
    public void setRegistrationId(Long registrationId) { this.registrationId = registrationId; }
    
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    
    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }
    
    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }
    
    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }
    
    public String getQrToken() { return qrToken; }
    public void setQrToken(String qrToken) { this.qrToken = qrToken; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Attendance getAttendance() { return attendance; }
    public void setAttendance(Attendance attendance) { this.attendance = attendance; }
    
    public Feedback getFeedback() { return feedback; }
    public void setFeedback(Feedback feedback) { this.feedback = feedback; }
    
    // Helper methods
    public boolean isActive() {
        return "REGISTERED".equals(status);
    }
    
    public boolean hasAttended() {
        return attendance != null;
    }
    
    public boolean hasFeedback() {
        return feedback != null;
    }
    
    @Override
    public String toString() {
        return "Registration{" +
                "registrationId=" + registrationId +
                ", studentId='" + (student != null ? student.getStudentId() : null) + '\'' +
                ", eventId='" + (event != null ? event.getEventId() : null) + '\'' +
                ", status='" + status + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
