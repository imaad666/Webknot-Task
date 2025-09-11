package com.webknot.campus.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.webknot.campus.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    
    // Find by registration ID
    Optional<Attendance> findByRegistrationRegistrationId(Long registrationId);
    
    // Find by student ID
    List<Attendance> findByStudentStudentId(String studentId);
    
    // Find by event ID
    List<Attendance> findByEventEventId(String eventId);
    
    // Find by check-in method
    List<Attendance> findByCheckInMethod(String checkInMethod);
    
    // Count attendance for an event
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.event.eventId = :eventId")
    Long countAttendanceByEvent(@Param("eventId") String eventId);
    
    // Count attendance for a student
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student.studentId = :studentId")
    Long countAttendanceByStudent(@Param("studentId") String studentId);
    
    // Get attendance between dates
    List<Attendance> findByCheckInTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    // Get attendance by QR scan method
    @Query("SELECT a FROM Attendance a WHERE a.checkInMethod = 'QR_SCAN'")
    List<Attendance> findQRScanAttendance();
    
    // Get attendance by manual method
    @Query("SELECT a FROM Attendance a WHERE a.checkInMethod = 'MANUAL'")
    List<Attendance> findManualAttendance();
    
    // Get attendance stats by event
    @Query("SELECT a.event.eventId, a.event.eventName, " +
           "COUNT(a) as totalAttendance, " +
           "COUNT(CASE WHEN a.checkInMethod = 'QR_SCAN' THEN 1 END) as qrScans, " +
           "COUNT(CASE WHEN a.checkInMethod = 'MANUAL' THEN 1 END) as manualCheckins " +
           "FROM Attendance a " +
           "GROUP BY a.event.eventId, a.event.eventName " +
           "ORDER BY totalAttendance DESC")
    List<Object[]> getAttendanceStatsByEvent();
    
    // Check if student attended specific event
    @Query("SELECT COUNT(a) > 0 FROM Attendance a " +
           "WHERE a.student.studentId = :studentId AND a.event.eventId = :eventId")
    boolean hasStudentAttendedEvent(@Param("studentId") String studentId, @Param("eventId") String eventId);
    
    // Get students who attended most events
    @Query("SELECT a.student.studentId, a.student.firstName, a.student.lastName, " +
           "COUNT(a) as attendanceCount " +
           "FROM Attendance a " +
           "GROUP BY a.student.studentId, a.student.firstName, a.student.lastName " +
           "ORDER BY attendanceCount DESC")
    List<Object[]> getMostActiveStudentsByAttendance();
    
    // Get attendance by event type
    @Query("SELECT a FROM Attendance a WHERE a.event.eventType = :eventType")
    List<Attendance> findByEventType(@Param("eventType") String eventType);
    
    // Get recent check-ins (last N hours)
    @Query("SELECT a FROM Attendance a " +
           "WHERE a.checkInTime >= :since " +
           "ORDER BY a.checkInTime DESC")
    List<Attendance> findRecentCheckIns(@Param("since") LocalDateTime since);
}
