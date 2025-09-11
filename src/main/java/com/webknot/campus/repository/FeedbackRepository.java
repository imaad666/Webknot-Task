package com.webknot.campus.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.webknot.campus.entity.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    
    // Find by registration ID
    Optional<Feedback> findByRegistrationRegistrationId(Long registrationId);
    
    // Find by student ID
    List<Feedback> findByStudentStudentId(String studentId);
    
    // Find by event ID
    List<Feedback> findByEventEventId(String eventId);
    
    // Find by rating
    List<Feedback> findByRating(Integer rating);
    
    // Find by rating range
    List<Feedback> findByRatingBetween(Integer minRating, Integer maxRating);
    
    // Count feedback for an event
    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.event.eventId = :eventId")
    Long countFeedbackByEvent(@Param("eventId") String eventId);
    
    // Get average rating for an event
    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.event.eventId = :eventId")
    Double getAverageRatingByEvent(@Param("eventId") String eventId);
    
    // Get feedback distribution for an event
    @Query("SELECT f.rating, COUNT(f) " +
           "FROM Feedback f " +
           "WHERE f.event.eventId = :eventId " +
           "GROUP BY f.rating " +
           "ORDER BY f.rating")
    List<Object[]> getFeedbackDistributionByEvent(@Param("eventId") String eventId);
    
    // Get events with highest average ratings
    @Query("SELECT f.event.eventId, f.event.eventName, AVG(f.rating) as avgRating, COUNT(f) as feedbackCount " +
           "FROM Feedback f " +
           "GROUP BY f.event.eventId, f.event.eventName " +
           "HAVING COUNT(f) >= :minFeedbackCount " +
           "ORDER BY avgRating DESC")
    List<Object[]> getEventsWithHighestRatings(@Param("minFeedbackCount") Long minFeedbackCount);
    
    // Get positive feedback (rating >= 4)
    @Query("SELECT f FROM Feedback f WHERE f.rating >= 4")
    List<Feedback> getPositiveFeedback();
    
    // Get negative feedback (rating <= 2)
    @Query("SELECT f FROM Feedback f WHERE f.rating <= 2")
    List<Feedback> getNegativeFeedback();
    
    // Get feedback with comments
    @Query("SELECT f FROM Feedback f WHERE f.comments IS NOT NULL AND TRIM(f.comments) != ''")
    List<Feedback> getFeedbackWithComments();
    
    // Get average rating by event type
    @Query("SELECT f.event.eventType, AVG(f.rating) as avgRating, COUNT(f) as feedbackCount " +
           "FROM Feedback f " +
           "GROUP BY f.event.eventType " +
           "ORDER BY avgRating DESC")
    List<Object[]> getAverageRatingByEventType();
    
    // Check if student has given feedback for event
    @Query("SELECT COUNT(f) > 0 FROM Feedback f " +
           "WHERE f.student.studentId = :studentId AND f.event.eventId = :eventId")
    boolean hasStudentGivenFeedback(@Param("studentId") String studentId, @Param("eventId") String eventId);
    
    // Get feedback statistics summary
    @Query("SELECT " +
           "COUNT(f) as totalFeedback, " +
           "AVG(f.rating) as averageRating, " +
           "MIN(f.rating) as minRating, " +
           "MAX(f.rating) as maxRating, " +
           "COUNT(CASE WHEN f.rating >= 4 THEN 1 END) as positiveFeedback, " +
           "COUNT(CASE WHEN f.rating <= 2 THEN 1 END) as negativeFeedback " +
           "FROM Feedback f")
    Object[] getFeedbackStatsSummary();
    
    // Get recent feedback (last N entries)
    @Query("SELECT f FROM Feedback f ORDER BY f.feedbackDate DESC")
    List<Feedback> findRecentFeedback();
    
    // Get feedback by event type
    @Query("SELECT f FROM Feedback f WHERE f.event.eventType = :eventType")
    List<Feedback> findByEventType(@Param("eventType") String eventType);
}
