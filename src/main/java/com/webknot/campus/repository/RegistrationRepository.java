package com.webknot.campus.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.webknot.campus.entity.Registration;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    
    // Find by QR token (for attendance scanning)
    Optional<Registration> findByQrToken(String qrToken);
    
    // Find by student ID
    List<Registration> findByStudentStudentId(String studentId);
    
    // Find by event ID
    List<Registration> findByEventEventId(String eventId);
    
    // Find by student and event
    Optional<Registration> findByStudentStudentIdAndEventEventId(String studentId, String eventId);
    
    // Find active registrations by student
    List<Registration> findByStudentStudentIdAndStatus(String studentId, String status);
    
    // Find active registrations by event
    List<Registration> findByEventEventIdAndStatus(String eventId, String status);
    
    // Count registrations for an event
    @Query("SELECT COUNT(r) FROM Registration r WHERE r.event.eventId = :eventId AND r.status = 'REGISTERED'")
    Long countActiveRegistrationsByEvent(@Param("eventId") String eventId);
    
    // Count registrations for a student
    @Query("SELECT COUNT(r) FROM Registration r WHERE r.student.studentId = :studentId AND r.status = 'REGISTERED'")
    Long countActiveRegistrationsByStudent(@Param("studentId") String studentId);
    
    // Check if student is already registered for event
    @Query("SELECT COUNT(r) > 0 FROM Registration r " +
           "WHERE r.student.studentId = :studentId AND r.event.eventId = :eventId AND r.status = 'REGISTERED'")
    boolean isStudentRegisteredForEvent(@Param("studentId") String studentId, @Param("eventId") String eventId);
    
    // Get registrations with attendance status
    @Query("SELECT r FROM Registration r " +
           "LEFT JOIN FETCH r.attendance " +
           "WHERE r.event.eventId = :eventId AND r.status = 'REGISTERED'")
    List<Registration> findRegistrationsWithAttendanceByEvent(@Param("eventId") String eventId);
    
    // Get registrations without attendance (who didn't show up)
    @Query("SELECT r FROM Registration r " +
           "WHERE r.event.eventId = :eventId AND r.status = 'REGISTERED' AND r.attendance IS NULL")
    List<Registration> findRegistrationsWithoutAttendance(@Param("eventId") String eventId);
    
    // Get registrations with attendance (who showed up)
    @Query("SELECT r FROM Registration r " +
           "WHERE r.event.eventId = :eventId AND r.status = 'REGISTERED' AND r.attendance IS NOT NULL")
    List<Registration> findRegistrationsWithAttendance(@Param("eventId") String eventId);
    
    // Get student participation report
    @Query("SELECT r.student.studentId, r.student.firstName, r.student.lastName, " +
           "COUNT(r) as totalRegistrations, " +
           "COUNT(r.attendance) as totalAttendance " +
           "FROM Registration r " +
           "WHERE r.status = 'REGISTERED' " +
           "GROUP BY r.student.studentId, r.student.firstName, r.student.lastName " +
           "ORDER BY totalAttendance DESC, totalRegistrations DESC")
    List<Object[]> getStudentParticipationReport();
    
    // Get registrations by event type
    @Query("SELECT r FROM Registration r " +
           "WHERE r.event.eventType = :eventType AND r.status = 'REGISTERED'")
    List<Registration> findByEventType(@Param("eventType") String eventType);
}
