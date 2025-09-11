package com.webknot.campus.dto;

import java.time.LocalDateTime;

public class FeedbackResponse {
    
    private Long feedbackId;
    private String studentId;
    private String studentName;
    private String eventId;
    private String eventName;
    private Integer rating;
    private String comments;
    private LocalDateTime feedbackDate;
    
    // Constructors
    public FeedbackResponse() {}
    
    // Getters and Setters
    public Long getFeedbackId() {
        return feedbackId;
    }
    
    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
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
    
    public Integer getRating() {
        return rating;
    }
    
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public String getComments() {
        return comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public LocalDateTime getFeedbackDate() {
        return feedbackDate;
    }
    
    public void setFeedbackDate(LocalDateTime feedbackDate) {
        this.feedbackDate = feedbackDate;
    }
    
    @Override
    public String toString() {
        return "FeedbackResponse{" +
                "feedbackId=" + feedbackId +
                ", studentId='" + studentId + '\'' +
                ", studentName='" + studentName + '\'' +
                ", eventId='" + eventId + '\'' +
                ", eventName='" + eventName + '\'' +
                ", rating=" + rating +
                ", feedbackDate=" + feedbackDate +
                '}';
    }
}
