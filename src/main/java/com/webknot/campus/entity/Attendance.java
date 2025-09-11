package com.webknot.campus.entity;

import java.time.LocalDateTime;

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
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "attendance")
public class Attendance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long attendanceId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_id", referencedColumnName = "registration_id", nullable = false, unique = true)
    private Registration registration;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "event_id", nullable = false)
    private Event event;
    
    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;
    
    @Column(name = "check_in_method", length = 20)
    @Pattern(regexp = "^(QR_SCAN|MANUAL)$", message = "Check-in method must be QR_SCAN or MANUAL")
    private String checkInMethod = "QR_SCAN";
    
    @Column(name = "scanned_by", length = 100)
    private String scannedBy; // Admin who scanned (optional)
    
    // Constructors
    public Attendance() {
        this.checkInTime = LocalDateTime.now();
        this.checkInMethod = "QR_SCAN";
    }
    
    public Attendance(Registration registration) {
        this();
        this.registration = registration;
        this.student = registration.getStudent();
        this.event = registration.getEvent();
    }
    
    public Attendance(Registration registration, String checkInMethod, String scannedBy) {
        this(registration);
        this.checkInMethod = checkInMethod;
        this.scannedBy = scannedBy;
    }
    
    @PrePersist
    protected void onCreate() {
        if (checkInTime == null) {
            this.checkInTime = LocalDateTime.now();
        }
    }
    
    // Getters and Setters
    public Long getAttendanceId() { return attendanceId; }
    public void setAttendanceId(Long attendanceId) { this.attendanceId = attendanceId; }
    
    public Registration getRegistration() { return registration; }
    public void setRegistration(Registration registration) { 
        this.registration = registration;
        if (registration != null) {
            this.student = registration.getStudent();
            this.event = registration.getEvent();
        }
    }
    
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    
    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }
    
    public LocalDateTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; }
    
    public String getCheckInMethod() { return checkInMethod; }
    public void setCheckInMethod(String checkInMethod) { this.checkInMethod = checkInMethod; }
    
    public String getScannedBy() { return scannedBy; }
    public void setScannedBy(String scannedBy) { this.scannedBy = scannedBy; }
    
    // Helper methods
    public boolean isQrScan() {
        return "QR_SCAN".equals(checkInMethod);
    }
    
    public boolean isManualCheckIn() {
        return "MANUAL".equals(checkInMethod);
    }
    
    @Override
    public String toString() {
        return "Attendance{" +
                "attendanceId=" + attendanceId +
                ", studentId='" + (student != null ? student.getStudentId() : null) + '\'' +
                ", eventId='" + (event != null ? event.getEventId() : null) + '\'' +
                ", checkInTime=" + checkInTime +
                ", checkInMethod='" + checkInMethod + '\'' +
                '}';
    }
}
