package com.webknot.campus.entity;

import java.time.LocalDateTime;
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
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "students")
public class Student {
    
    @Id
    @Column(name = "student_id", length = 20)
    private String studentId; // 1WKU21CS001
    
    @Column(name = "first_name", nullable = false, length = 100)
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 100)
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    @Column(name = "email", unique = true, nullable = false)
    @Email(message = "Valid email is required")
    @NotBlank(message = "Email is required")
    private String email;
    
    @Column(name = "phone", length = 15)
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phone;
    
    @Column(name = "course", nullable = false, length = 10)
    @NotBlank(message = "Course is required")
    private String course; // CS, IT, ME, etc.
    
    @Column(name = "year_of_joining", nullable = false)
    @Min(value = 2000, message = "Year of joining must be valid")
    @Max(value = 2030, message = "Year of joining must be valid")
    private Integer yearOfJoining;
    
    @Column(name = "roll_number", nullable = false)
    @Min(value = 1, message = "Roll number must be positive")
    private Integer rollNumber;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Registration> registrations;
    
    // Constructors
    public Student() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Student(String firstName, String lastName, String email, String phone, 
                   String course, Integer yearOfJoining, Integer rollNumber) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.course = course;
        this.yearOfJoining = yearOfJoining;
        this.rollNumber = rollNumber;
        this.studentId = generateStudentId();
    }
    
    @PrePersist
    protected void onCreate() {
        if (studentId == null) {
            this.studentId = generateStudentId();
        }
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    private String generateStudentId() {
        // Format: 1WKU<year><course><roll>
        // Example: 1WKU21CS001
        String yearSuffix = String.valueOf(yearOfJoining % 100); // 2021 -> 21
        String rollFormatted = String.format("%03d", rollNumber); // 1 -> 001
        return "1WKU" + yearSuffix + course.toUpperCase() + rollFormatted;
    }
    
    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
    
    public Integer getYearOfJoining() { return yearOfJoining; }
    public void setYearOfJoining(Integer yearOfJoining) { this.yearOfJoining = yearOfJoining; }
    
    public Integer getRollNumber() { return rollNumber; }
    public void setRollNumber(Integer rollNumber) { this.rollNumber = rollNumber; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<Registration> getRegistrations() { return registrations; }
    public void setRegistrations(List<Registration> registrations) { this.registrations = registrations; }
    
    // Helper methods
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", course='" + course + '\'' +
                ", yearOfJoining=" + yearOfJoining +
                '}';
    }
}
