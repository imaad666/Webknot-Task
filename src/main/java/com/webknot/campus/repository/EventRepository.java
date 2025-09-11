package com.webknot.campus.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.webknot.campus.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {
    
    // Find by event type
    List<Event> findByEventType(String eventType);
    
    // Find by event date
    List<Event> findByEventDate(LocalDate eventDate);
    
    // Find events between dates
    List<Event> findByEventDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Find upcoming events
    @Query("SELECT e FROM Event e WHERE e.eventDate >= CURRENT_DATE ORDER BY e.eventDate ASC")
    List<Event> findUpcomingEvents();
    
    // Find past events
    @Query("SELECT e FROM Event e WHERE e.eventDate < CURRENT_DATE ORDER BY e.eventDate DESC")
    List<Event> findPastEvents();
    
    // Find events by organizer
    List<Event> findByOrganizerEmail(String organizerEmail);
    
    // Find events with available slots
    @Query("SELECT e FROM Event e " +
           "WHERE e.capacity > " +
           "(SELECT COUNT(r) FROM Registration r WHERE r.event = e AND r.status = 'REGISTERED')")
    List<Event> findEventsWithAvailableSlots();
    
    // Get events ordered by registration count (popularity)
    @Query("SELECT e FROM Event e " +
           "LEFT JOIN e.registrations r " +
           "WHERE r.status = 'REGISTERED' OR r.status IS NULL " +
           "GROUP BY e " +
           "ORDER BY COUNT(r) DESC")
    List<Event> findEventsOrderByPopularity();
    
    // Get events by type ordered by popularity
    @Query("SELECT e FROM Event e " +
           "LEFT JOIN e.registrations r " +
           "WHERE e.eventType = :eventType AND (r.status = 'REGISTERED' OR r.status IS NULL) " +
           "GROUP BY e " +
           "ORDER BY COUNT(r) DESC")
    List<Event> findEventsByTypeOrderByPopularity(@Param("eventType") String eventType);
    
    // Get registration count for each event
    @Query("SELECT e.eventId, e.eventName, COUNT(r) as registrationCount " +
           "FROM Event e " +
           "LEFT JOIN e.registrations r " +
           "WHERE r.status = 'REGISTERED' OR r.status IS NULL " +
           "GROUP BY e.eventId, e.eventName " +
           "ORDER BY COUNT(r) DESC")
    List<Object[]> getEventRegistrationCounts();
    
    // Get attendance percentage for events
    @Query("SELECT e.eventId, e.eventName, " +
           "COUNT(r) as totalRegistrations, " +
           "COUNT(a) as totalAttendance, " +
           "CASE WHEN COUNT(r) > 0 THEN (COUNT(a) * 100.0 / COUNT(r)) ELSE 0 END as attendancePercentage " +
           "FROM Event e " +
           "LEFT JOIN e.registrations r ON r.status = 'REGISTERED' " +
           "LEFT JOIN r.attendance a " +
           "GROUP BY e.eventId, e.eventName " +
           "ORDER BY attendancePercentage DESC")
    List<Object[]> getEventAttendanceStats();
    
    // Get average feedback score for events
    @Query("SELECT e.eventId, e.eventName, " +
           "AVG(f.rating) as averageRating, " +
           "COUNT(f) as feedbackCount " +
           "FROM Event e " +
           "LEFT JOIN e.registrations r " +
           "LEFT JOIN r.feedback f " +
           "GROUP BY e.eventId, e.eventName " +
           "ORDER BY averageRating DESC")
    List<Object[]> getEventFeedbackStats();
    
    // Search events by name
    @Query("SELECT e FROM Event e " +
           "WHERE LOWER(e.eventName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Event> searchByEventName(@Param("name") String name);
    
    // Count active registrations by event
    @Query("SELECT COUNT(r) FROM Registration r WHERE r.event.eventId = :eventId AND r.status = 'REGISTERED'")
    Long countActiveRegistrationsByEvent(@Param("eventId") String eventId);
}
