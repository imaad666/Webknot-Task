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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "feedback")
public class Feedback {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Long feedbackId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_id", referencedColumnName = "registration_id", nullable = false, unique = true)
    private Registration registration;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "event_id", nullable = false)
    private Event event;
    
    @Column(name = "rating", nullable = false)
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Integer rating;
    
    @Column(name = "comments", columnDefinition = "TEXT")
    @Size(max = 1000, message = "Comments must not exceed 1000 characters")
    private String comments;
    
    @Column(name = "feedback_date")
    private LocalDateTime feedbackDate;
    
    // Constructors
    public Feedback() {
        this.feedbackDate = LocalDateTime.now();
    }
    
    public Feedback(Registration registration, Integer rating, String comments) {
        this();
        this.registration = registration;
        this.student = registration.getStudent();
        this.event = registration.getEvent();
        this.rating = rating;
        this.comments = comments;
    }
    
    @PrePersist
    protected void onCreate() {
        if (feedbackDate == null) {
            this.feedbackDate = LocalDateTime.now();
        }
    }
    
    // Getters and Setters
    public Long getFeedbackId() { return feedbackId; }
    public void setFeedbackId(Long feedbackId) { this.feedbackId = feedbackId; }
    
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
    
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    
    public LocalDateTime getFeedbackDate() { return feedbackDate; }
    public void setFeedbackDate(LocalDateTime feedbackDate) { this.feedbackDate = feedbackDate; }
    
    // Helper methods
    public boolean isPositiveFeedback() {
        return rating != null && rating >= 4;
    }
    
    public boolean isNegativeFeedback() {
        return rating != null && rating <= 2;
    }
    
    public String getRatingDescription() {
        if (rating == null) return "No rating";
        return switch (rating) {
            case 1 -> "Very Poor";
            case 2 -> "Poor";
            case 3 -> "Average";
            case 4 -> "Good";
            case 5 -> "Excellent";
            default -> "Unknown";
        };
    }
    
    @Override
    public String toString() {
        return "Feedback{" +
                "feedbackId=" + feedbackId +
                ", studentId='" + (student != null ? student.getStudentId() : null) + '\'' +
                ", eventId='" + (event != null ? event.getEventId() : null) + '\'' +
                ", rating=" + rating +
                ", feedbackDate=" + feedbackDate +
                '}';
    }
}
