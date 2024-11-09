package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.entity.Event;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;
    private Event event;

    @BeforeEach
    public void setUp() {
        // Set up a sample event before each test
        event = new Event();
        event.setName("Sample Event");
    }

    @Test
    public void testSaveEvent() {
        // Save the event to the repository
        Event savedEvent = eventRepository.save(event);

        // Verify that the event is saved and has a generated ID
        Assertions.assertThat(savedEvent).isNotNull();
        Assertions.assertThat(savedEvent.getId()).isGreaterThan(0);
        Assertions.assertThat(savedEvent.getName()).isEqualTo("Sample Event");
    }

    @Test
    public void testFindEventById() {
        // Save an event first
        Event savedEvent = eventRepository.save(event);

        // Find the event by its ID
        Optional<Event> foundEvent = eventRepository.findById(savedEvent.getId());

        // Verify that the event is found
        Assertions.assertThat(foundEvent).isPresent();
        Assertions.assertThat(foundEvent.get().getName()).isEqualTo("Sample Event");
    }

    @Test
    public void testFindAllEvents() {
        // Save multiple events
        Event event2 = new Event();
        event2.setName("Another Test Event");
        eventRepository.save(event);
        eventRepository.save(event2);

        // Retrieve all events
        List<Event> events = eventRepository.findAll();

        // Verify that the events are saved and found
        Assertions.assertThat(events).hasSize(2);
        Assertions.assertThat(events).extracting(Event::getName).contains("Sample Event", "Another Test Event");
    }

    @Test
    public void testDeleteEvent() {
        // Save an event
        Event savedEvent = eventRepository.save(event);

        // Delete the event by its ID
        eventRepository.deleteById(savedEvent.getId());

        // Verify that the event is deleted
        Optional<Event> foundEvent = eventRepository.findById(savedEvent.getId());
        Assertions.assertThat(foundEvent).isNotPresent();
    }

    @Test
    public void testEventNotFound() {
        // Try to find an event with a non-existing ID
        Optional<Event> foundEvent = eventRepository.findById(999L);

        // Verify that no event is found
        Assertions.assertThat(foundEvent).isNotPresent();
    }
}
