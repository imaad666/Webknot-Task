package com.webknot.campus.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.webknot.campus.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    
    // Find by email
    Optional<Student> findByEmail(String email);
    
    // Find by course
    List<Student> findByCourse(String course);
    
    // Find by year of joining
    List<Student> findByYearOfJoining(Integer year);
    
    // Find by course and year
    List<Student> findByCourseAndYearOfJoining(String course, Integer year);
    
    // Check if email exists
    boolean existsByEmail(String email);
    
    // Check if student ID exists
    boolean existsByStudentId(String studentId);
    
    // Find by course and roll number to check for duplicates
    Optional<Student> findByCourseAndYearOfJoiningAndRollNumber(String course, Integer year, Integer rollNumber);
    
    // Get students by registration count (most active students)
    @Query("SELECT s FROM Student s " +
           "JOIN s.registrations r " +
           "WHERE r.status = 'REGISTERED' " +
           "GROUP BY s " +
           "ORDER BY COUNT(r) DESC")
    List<Student> findStudentsOrderByRegistrationCount();
    
    // Get top N most active students
    @Query("SELECT s FROM Student s " +
           "JOIN s.registrations r " +
           "WHERE r.status = 'REGISTERED' " +
           "GROUP BY s " +
           "ORDER BY COUNT(r) DESC")
    List<Student> findTopActiveStudents(@Param("limit") int limit);
    
    // Count active registrations by student
    @Query("SELECT COUNT(r) FROM Registration r WHERE r.student.studentId = :studentId AND r.status = 'REGISTERED'")
    Long countActiveRegistrationsByStudent(@Param("studentId") String studentId);
    
    // Get students who attended specific number of events
    @Query("SELECT s FROM Student s " +
           "JOIN s.registrations r " +
           "JOIN r.attendance a " +
           "GROUP BY s " +
           "HAVING COUNT(a) >= :minAttendance " +
           "ORDER BY COUNT(a) DESC")
    List<Student> findStudentsByMinAttendance(@Param("minAttendance") int minAttendance);
    
    // Search students by name (first or last name)
    @Query("SELECT s FROM Student s " +
           "WHERE LOWER(s.firstName) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Student> searchByName(@Param("name") String name);
}
