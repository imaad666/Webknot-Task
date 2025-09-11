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

import com.webknot.campus.dto.FeedbackRequest;
import com.webknot.campus.dto.FeedbackResponse;
import com.webknot.campus.entity.Feedback;
import com.webknot.campus.entity.Registration;
import com.webknot.campus.repository.FeedbackRepository;
import com.webknot.campus.repository.RegistrationRepository;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "*")
public class FeedbackController {
    
    @Autowired
    private FeedbackRepository feedbackRepository;
    
    @Autowired
    private RegistrationRepository registrationRepository;
    
    /**
     * Submit feedback for an event
     */
    @PostMapping
    public ResponseEntity<FeedbackResponse> submitFeedback(@RequestBody FeedbackRequest request) {
        try {
            // Find registration
            Optional<Registration> registrationOpt = registrationRepository
                .findByStudentStudentIdAndEventEventId(request.getStudentId(), request.getEventId());
                
            if (registrationOpt.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            Registration registration = registrationOpt.get();
            
            // Check if feedback already exists
            if (feedbackRepository.findByRegistrationRegistrationId(registration.getRegistrationId()).isPresent()) {
                return ResponseEntity.badRequest().build();
            }
            
            // Create feedback
            Feedback feedback = new Feedback(registration, request.getRating(), request.getComments());
            feedback = feedbackRepository.save(feedback);
            
            // Create response
            FeedbackResponse response = new FeedbackResponse();
            response.setFeedbackId(feedback.getFeedbackId());
            response.setStudentId(feedback.getStudent().getStudentId());
            response.setStudentName(feedback.getStudent().getFullName());
            response.setEventId(feedback.getEvent().getEventId());
            response.setEventName(feedback.getEvent().getEventName());
            response.setRating(feedback.getRating());
            response.setComments(feedback.getComments());
            response.setFeedbackDate(feedback.getFeedbackDate());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get all feedback
     */
    @GetMapping
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }
    
    /**
     * Get feedback by event
     */
    @GetMapping("/event/{eventId}")
    public List<Feedback> getFeedbackByEvent(@PathVariable String eventId) {
        return feedbackRepository.findByEventEventId(eventId);
    }
    
    /**
     * Get feedback by student
     */
    @GetMapping("/student/{studentId}")
    public List<Feedback> getFeedbackByStudent(@PathVariable String studentId) {
        return feedbackRepository.findByStudentStudentId(studentId);
    }
    
    /**
     * Get average rating for an event
     */
    @GetMapping("/average/{eventId}")
    public ResponseEntity<Double> getAverageRating(@PathVariable String eventId) {
        Double avgRating = feedbackRepository.getAverageRatingByEvent(eventId);
        return ResponseEntity.ok(avgRating != null ? avgRating : 0.0);
    }
    
    /**
     * Get feedback distribution for an event
     */
    @GetMapping("/distribution/{eventId}")
    public List<Object[]> getFeedbackDistribution(@PathVariable String eventId) {
        return feedbackRepository.getFeedbackDistributionByEvent(eventId);
    }
    
    /**
     * Get positive feedback (rating >= 4)
     */
    @GetMapping("/positive")
    public List<Feedback> getPositiveFeedback() {
        return feedbackRepository.getPositiveFeedback();
    }
    
    /**
     * Get negative feedback (rating <= 2)
     */
    @GetMapping("/negative")
    public List<Feedback> getNegativeFeedback() {
        return feedbackRepository.getNegativeFeedback();
    }
    
    /**
     * Check if student has given feedback for event
     */
    @GetMapping("/check/{studentId}/{eventId}")
    public ResponseEntity<Boolean> checkFeedbackExists(@PathVariable String studentId, @PathVariable String eventId) {
        boolean exists = feedbackRepository.hasStudentGivenFeedback(studentId, eventId);
        return ResponseEntity.ok(exists);
    }
}
