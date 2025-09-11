package com.webknot.campus.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "events")
public class Event {
    
    @Id
    @Column(name = "event_id", length = 20)
    private String eventId; // WKUe120225
    
    @Column(name = "event_name", nullable = false)
    @NotBlank(message = "Event name is required")
    @Size(max = 255, message = "Event name must not exceed 255 characters")
    private String eventName;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "event_type", nullable = false, length = 20)
    @NotBlank(message = "Event type is required")
    @Pattern(regexp = "^(Workshop|Fest|Seminar|Hackathon|Tech Talk)$", message = "Event type must be Workshop, Fest, Seminar, Hackathon, or Tech Talk")
    private String eventType;
    
    @Column(name = "event_date", nullable = false)
    @NotNull(message = "Event date is required")
    // @Future(message = "Event date must be in the future") - Removed for demo purposes
    private LocalDate eventDate;
    
    @Column(name = "event_time", nullable = false)
    @NotNull(message = "Event time is required")
    private LocalTime eventTime;
    
    @Column(name = "venue", nullable = false)
    @NotBlank(message = "Venue is required")
    @Size(max = 255, message = "Venue must not exceed 255 characters")
    private String venue;
    
    @Column(name = "capacity", nullable = false)
    @Min(value = 1, message = "Capacity must be at least 1")
    @Max(value = 1000, message = "Capacity must not exceed 1000")
    private Integer capacity;
    
    @Column(name = "organizer_name", length = 100)
    private String organizerName;
    
    @Column(name = "organizer_email")
    @Email(message = "Valid organizer email is required")
    private String organizerEmail;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Registration> registrations;
    
    // Constructors
    public Event() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Event(String eventName, String description, String eventType, LocalDate eventDate, 
                 LocalTime eventTime, String venue, Integer capacity, String organizerName, String organizerEmail) {
        this();
        this.eventName = eventName;
        this.description = description;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.venue = venue;
        this.capacity = capacity;
        this.organizerName = organizerName;
        this.organizerEmail = organizerEmail;
        // ID will be generated in @PrePersist after all fields are set
    }
    
    @PrePersist
    protected void onCreate() {
        if (eventId == null) {
            this.eventId = generateEventId();
        }
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    private String generateEventId() {
        // Format: WKUe<DDMMYY><HHMM><timestamp>
        // Example: WKUe150925100012345 for 15th Sept 2025 at 10:00 with timestamp
        LocalDate date = eventDate != null ? eventDate : LocalDate.now();
        LocalTime time = eventTime != null ? eventTime : LocalTime.now();
        
        String day = String.format("%02d", date.getDayOfMonth());
        String month = String.format("%02d", date.getMonthValue());
        String year = String.valueOf(date.getYear() % 100);
        String hour = String.format("%02d", time.getHour());
        String minute = String.format("%02d", time.getMinute());
        String timestamp = String.valueOf(System.currentTimeMillis() % 100000); // Last 5 digits
        
        return "WKUe" + day + month + year + hour + minute + timestamp;
    }
    
    // Helper method to get current registration count
    public int getCurrentRegistrationCount() {
        if (registrations == null) {
            return 0;
        }
        return (int) registrations.stream()
                .filter(registration -> "REGISTERED".equals(registration.getStatus()))
                .count();
    }
    
    // Getters and Setters
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    
    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
    
    public LocalTime getEventTime() { return eventTime; }
    public void setEventTime(LocalTime eventTime) { this.eventTime = eventTime; }
    
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    
    public String getOrganizerName() { return organizerName; }
    public void setOrganizerName(String organizerName) { this.organizerName = organizerName; }
    
    public String getOrganizerEmail() { return organizerEmail; }
    public void setOrganizerEmail(String organizerEmail) { this.organizerEmail = organizerEmail; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<Registration> getRegistrations() { return registrations; }
    public void setRegistrations(List<Registration> registrations) { this.registrations = registrations; }
    
    // Helper methods
    public int getCurrentRegistrationCount() {
        return registrations != null ? (int) registrations.stream()
                .filter(reg -> "REGISTERED".equals(reg.getStatus()))
                .count() : 0;
    }
    
    public boolean isFullyBooked() {
        return getCurrentRegistrationCount() >= capacity;
    }
    
    public int getAvailableSlots() {
        return capacity - getCurrentRegistrationCount();
    }
    
    @Override
    public String toString() {
        return "Event{" +
                "eventId='" + eventId + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventType='" + eventType + '\'' +
                ", eventDate=" + eventDate +
                ", venue='" + venue + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}
