package com.webknot.campus.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webknot.campus.entity.Student;
import com.webknot.campus.repository.StudentRepository;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {
    
    @Autowired
    private StudentRepository studentRepository;
    
    /**
     * Get all students
     */
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    /**
     * Get student by ID
     */
    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable String studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        return student.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Create new student
     */
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        try {
            // Check if email already exists
            if (studentRepository.existsByEmail(student.getEmail())) {
                return ResponseEntity.badRequest().build();
            }
            
            // Check if student with same course, year, and roll number exists
            Optional<Student> existingStudent = studentRepository
                .findByCourseAndYearOfJoiningAndRollNumber(
                    student.getCourse(), 
                    student.getYearOfJoining(), 
                    student.getRollNumber()
                );
            
            if (existingStudent.isPresent()) {
                return ResponseEntity.badRequest().build();
            }
            
            Student savedStudent = studentRepository.save(student);
            return ResponseEntity.ok(savedStudent);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get students by course
     */
    @GetMapping("/course/{course}")
    public List<Student> getStudentsByCourse(@PathVariable String course) {
        return studentRepository.findByCourse(course.toUpperCase());
    }
    
    /**
     * Get students by year
     */
    @GetMapping("/year/{year}")
    public List<Student> getStudentsByYear(@PathVariable Integer year) {
        return studentRepository.findByYearOfJoining(year);
    }
    
    /**
     * Get student's registration count
     */
    @GetMapping("/{studentId}/registration-count")
    public ResponseEntity<Long> getStudentRegistrationCount(@PathVariable String studentId) {
        if (!studentRepository.existsByStudentId(studentId)) {
            return ResponseEntity.notFound().build();
        }
        
        Long count = studentRepository.countActiveRegistrationsByStudent(studentId);
        return ResponseEntity.ok(count);
    }
}
