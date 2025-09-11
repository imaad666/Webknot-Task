package com.webknot.campus.dto;

import java.time.LocalDateTime;

public class AttendanceResponse {
    
    private Long attendanceId;
    private String studentId;
    private String studentName;
    private String eventId;
    private String eventName;
    private LocalDateTime checkInTime;
    private String checkInMethod;
    private String scannedBy;
    
    // Constructors
    public AttendanceResponse() {}
    
    // Getters and Setters
    public Long getAttendanceId() {
        return attendanceId;
    }
    
    public void setAttendanceId(Long attendanceId) {
        this.attendanceId = attendanceId;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getStudentName() {
        return studentName;
    }
    
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    
    public String getEventId() {
        return eventId;
    }
    
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    
    public String getEventName() {
        return eventName;
    }
    
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    
    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }
    
    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }
    
    public String getCheckInMethod() {
        return checkInMethod;
    }
    
    public void setCheckInMethod(String checkInMethod) {
        this.checkInMethod = checkInMethod;
    }
    
    public String getScannedBy() {
        return scannedBy;
    }
    
    public void setScannedBy(String scannedBy) {
        this.scannedBy = scannedBy;
    }
    
    @Override
    public String toString() {
        return "AttendanceResponse{" +
                "attendanceId=" + attendanceId +
                ", studentId='" + studentId + '\'' +
                ", studentName='" + studentName + '\'' +
                ", eventId='" + eventId + '\'' +
                ", eventName='" + eventName + '\'' +
                ", checkInTime=" + checkInTime +
                ", checkInMethod='" + checkInMethod + '\'' +
                '}';
    }
}
