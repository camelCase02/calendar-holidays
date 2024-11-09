package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Event;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.EventService;

@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/events")
    public List<Event> getEvents() {
        System.out.println(LocalDateTime.now());
        return eventService.getEvent().orElseThrow(() -> new ResourceNotFoundException("Error fetching All Events"));
    }

    @GetMapping("/events/{id}")
    public Event getEventById(@PathVariable(name = "id") Long id) {

        if (id <= 0) {
            throw new InvalidInputException("Id must be greater than 0");
        }

        return eventService.getEventById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + id));
    }

    @PostMapping("/events")
    public Event saveEvent(@RequestBody Event event) {
        return eventService.saveEvent(event).orElseThrow(() -> new ResourceNotFoundException("Event not saved"));
    }

    @PutMapping("/events")
    public Event updateEvent(@RequestBody Event event) {
        return eventService.editEvent(event).orElseThrow(() -> new ResourceNotFoundException("Event not updated"));
    }

    @DeleteMapping("/events/{id}")
    public void deleteEvent(@PathVariable(name = "id") Long id) {
        if (id <= 0) {
            throw new InvalidInputException("Id must be greater than 0");
        }
        try {
            eventService.deleteEvent(id);
        } catch (Exception e) {
            throw e;
        }
    }
}
