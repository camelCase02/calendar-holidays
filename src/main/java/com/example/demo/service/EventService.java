package com.example.demo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Event;
import com.example.demo.repository.EventRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Optional<List<Event>> getEvent() {
        return Optional.of(eventRepository.findAll());
    }

    public Optional<Event> getEventById(Long id) {
        try {
            return Optional.of(eventRepository.findById(id).get());
        } catch (Exception e) {
            throw e;
        }
    }

    public Optional<Event> saveEvent(Event event) {
        return Optional.of(eventRepository.save(event));
    }

    public Optional<Event> editEvent(Event event) {
        try {
            Event existingEvent = eventRepository.findById(event.getId()).get();
            existingEvent.setName(event.getName());
            existingEvent.setDescription(event.getDescription());
            existingEvent.setStartDate(event.getStartDate());
            existingEvent.setEndDate(event.getEndDate());
            eventRepository.save(existingEvent);
            return Optional.of(existingEvent);
        } catch (Exception e) {
            throw e;
        }
    }

    public void deleteEvent(Long id) {
        try {
            eventRepository.deleteById(id);
        } catch (Exception e) {
            throw e;
        }
    }

    public Optional<List<Event>> findByMonth(int year, int month) {
        try {
            final List<Event> allEvents = eventRepository.findAll();
            List<Event> eventsForMonth = new ArrayList<>();
            for (Event event : allEvents) {
                if (event.getStartDate().getYear() == year && event.getStartDate().getMonthValue() == month) {
                    eventsForMonth.add(event);
                }
            }

            return Optional.of(eventsForMonth);
        } catch (Exception e) {
            throw e;
        }
    }

    public Optional<List<Event>> getEventsForDate(LocalDate date, List<Event> events) {
        try {
            List<Event> eventsOnDate = new ArrayList<>();
            for (Event event : events) {
                if (!date.isBefore(event.getStartDate().toLocalDate())
                        && !date.isAfter(event.getEndDate().toLocalDate())) {
                    eventsOnDate.add(event);
                }
            }
            return Optional.of(eventsOnDate);
        } catch (Exception e) {
            throw e;

        }
    }
}
