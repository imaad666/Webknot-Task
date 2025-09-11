package com.webknot.campus.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FeedbackRequest {
    
    @NotBlank(message = "Student ID is required")
    private String studentId;
    
    @NotBlank(message = "Event ID is required")
    private String eventId;
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Integer rating;
    
    private String comments;
    
    // Constructors
    public FeedbackRequest() {}
    
    public FeedbackRequest(String studentId, String eventId, Integer rating, String comments) {
        this.studentId = studentId;
        this.eventId = eventId;
        this.rating = rating;
        this.comments = comments;
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
    
    @Override
    public String toString() {
        return "FeedbackRequest{" +
                "studentId='" + studentId + '\'' +
                ", eventId='" + eventId + '\'' +
                ", rating=" + rating +
                ", comments='" + comments + '\'' +
                '}';
    }
}
