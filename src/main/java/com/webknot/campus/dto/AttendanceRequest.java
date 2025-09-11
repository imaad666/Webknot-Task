package com.webknot.campus.dto;

public class AttendanceRequest {
    
    private String qrToken;      // For QR scan attendance
    private String studentId;    // For manual attendance
    private String eventId;      // For manual attendance
    private String scannedBy;    // Admin who scanned/marked
    
    // Constructors
    public AttendanceRequest() {}
    
    public AttendanceRequest(String qrToken, String scannedBy) {
        this.qrToken = qrToken;
        this.scannedBy = scannedBy;
    }
    
    public AttendanceRequest(String studentId, String eventId, String scannedBy) {
        this.studentId = studentId;
        this.eventId = eventId;
        this.scannedBy = scannedBy;
    }
    
    // Getters and Setters
    public String getQrToken() {
        return qrToken;
    }
    
    public void setQrToken(String qrToken) {
        this.qrToken = qrToken;
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
    
    public String getScannedBy() {
        return scannedBy;
    }
    
    public void setScannedBy(String scannedBy) {
        this.scannedBy = scannedBy;
    }
    
    @Override
    public String toString() {
        return "AttendanceRequest{" +
                "qrToken='" + qrToken + '\'' +
                ", studentId='" + studentId + '\'' +
                ", eventId='" + eventId + '\'' +
                ", scannedBy='" + scannedBy + '\'' +
                '}';
    }
}
