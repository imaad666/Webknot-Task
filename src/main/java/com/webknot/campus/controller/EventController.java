package com.webknot.campus.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webknot.campus.entity.Event;
import com.webknot.campus.repository.EventRepository;
import com.webknot.campus.repository.RegistrationRepository;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private RegistrationRepository registrationRepository;
    
    /**
     * Get all events with registration counts
     */
    @GetMapping
    public List<Map<String, Object>> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        
        return events.stream().map(event -> {
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("eventId", event.getEventId());
            eventData.put("eventName", event.getEventName());
            eventData.put("eventType", event.getEventType());
            eventData.put("eventDate", event.getEventDate());
            eventData.put("eventTime", event.getEventTime());
            eventData.put("venue", event.getVenue());
            eventData.put("capacity", event.getCapacity());
            eventData.put("organizerName", event.getOrganizerName());
            eventData.put("organizerEmail", event.getOrganizerEmail());
            eventData.put("description", event.getDescription());
            eventData.put("createdAt", event.getCreatedAt());
            eventData.put("updatedAt", event.getUpdatedAt());
            
            // Get registration count from repository
            long registrationCount = registrationRepository.countActiveRegistrationsByEvent(event.getEventId());
            eventData.put("registrationCount", registrationCount);
            
            return eventData;
        }).collect(Collectors.toList());
    }
    
    /**
     * Get event by ID
     */
    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEventById(@PathVariable String eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        return event.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Create new event
     */
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        try {
            System.out.println("Received event: " + event);
            System.out.println("Event name: " + event.getEventName());
            System.out.println("Event type: " + event.getEventType());
            System.out.println("Event date: " + event.getEventDate());
            System.out.println("Event time: " + event.getEventTime());
            System.out.println("Venue: " + event.getVenue());
            System.out.println("Capacity: " + event.getCapacity());
            
            Event savedEvent = eventRepository.save(event);
            return ResponseEntity.ok(savedEvent);
        } catch (Exception e) {
            System.err.println("Error creating event: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Simple test endpoint to create event manually
     */
    @PostMapping("/test")
    public ResponseEntity<String> createTestEvent() {
        try {
            Event event = new Event();
            event.setEventName("Test Event");
            event.setDescription("Test Description");
            event.setEventType("Workshop");
            event.setEventDate(java.time.LocalDate.parse("2025-09-15"));
            event.setEventTime(java.time.LocalTime.parse("10:00"));
            event.setVenue("Test Venue");
            event.setCapacity(50);
            event.setOrganizerName("Test Organizer");
            event.setOrganizerEmail("test@test.com");
            
            Event savedEvent = eventRepository.save(event);
            return ResponseEntity.ok("Event created successfully: " + savedEvent.getEventId());
        } catch (Exception e) {
            System.err.println("Error in test creation: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Delete event by ID
     */
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String eventId) {
        try {
            if (!eventRepository.existsById(eventId)) {
                return ResponseEntity.notFound().build();
            }
            
            eventRepository.deleteById(eventId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get upcoming events
     */
    @GetMapping("/upcoming")
    public List<Event> getUpcomingEvents() {
        return eventRepository.findUpcomingEvents();
    }
    
    /**
     * Get events by type
     */
    @GetMapping("/type/{eventType}")
    public List<Event> getEventsByType(@PathVariable String eventType) {
        return eventRepository.findByEventType(eventType);
    }
    
    /**
     * Get events with available slots
     */
    @GetMapping("/available")
    public List<Event> getEventsWithAvailableSlots() {
        return eventRepository.findEventsWithAvailableSlots();
    }
    
    /**
     * Get event registration count
     */
    @GetMapping("/{eventId}/registration-count")
    public ResponseEntity<Long> getEventRegistrationCount(@PathVariable String eventId) {
        if (!eventRepository.existsById(eventId)) {
            return ResponseEntity.notFound().build();
        }
        
        Long count = eventRepository.countActiveRegistrationsByEvent(eventId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Get event attendance percentage
     */
    @GetMapping("/{eventId}/attendance-percentage")
    public ResponseEntity<Double> getEventAttendancePercentage(@PathVariable String eventId) {
        if (!eventRepository.existsById(eventId)) {
            return ResponseEntity.notFound().build();
        }
        
        List<Object[]> stats = eventRepository.getEventAttendanceStats();
        Optional<Object[]> eventStats = stats.stream()
            .filter(stat -> eventId.equals(stat[0]))
            .findFirst();
            
        if (eventStats.isPresent()) {
            Double percentage = (Double) eventStats.get()[4];
            return ResponseEntity.ok(percentage != null ? percentage : 0.0);
        }
        
        return ResponseEntity.ok(0.0);
    }
}
