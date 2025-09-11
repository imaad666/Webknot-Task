package com.webknot.campus.dto;

import jakarta.validation.constraints.NotBlank;

public class RegistrationRequest {
    
    @NotBlank(message = "Student ID is required")
    private String studentId;
    
    @NotBlank(message = "Event ID is required")
    private String eventId;
    
    // Constructors
    public RegistrationRequest() {}
    
    public RegistrationRequest(String studentId, String eventId) {
        this.studentId = studentId;
        this.eventId = eventId;
    }
    
    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getEventId() {
        return eventId;
    }
    
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    
    @Override
    public String toString() {
        return "RegistrationRequest{" +
                "studentId='" + studentId + '\'' +
                ", eventId='" + eventId + '\'' +
                '}';
    }
}
