package com.webknot.campus.dto;

import java.time.LocalDateTime;

public class RegistrationResponse {
    
    private Long registrationId;
    private String studentId;
    private String eventId;
    private String qrCode; // Base64 encoded QR code
    private String qrToken;
    private String status;
    private LocalDateTime registrationDate;
    
    // Constructors
    public RegistrationResponse() {}
    
    // Getters and Setters
    public Long getRegistrationId() {
        return registrationId;
    }
    
    public void setRegistrationId(Long registrationId) {
        this.registrationId = registrationId;
    }
    
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
    
    public String getQrCode() {
        return qrCode;
    }
    
    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
    
    public String getQrToken() {
        return qrToken;
    }
    
    public void setQrToken(String qrToken) {
        this.qrToken = qrToken;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }
    
    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }
    
    @Override
    public String toString() {
        return "RegistrationResponse{" +
                "registrationId=" + registrationId +
                ", studentId='" + studentId + '\'' +
                ", eventId='" + eventId + '\'' +
                ", status='" + status + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
