package com.example.demo.service;

import com.example.demo.entity.Event;
import com.example.demo.repository.EventRepository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    public void testGetEvent() {
        // Given
        Event event1 = new Event();
        event1.setName("Event 1");
        Event event2 = new Event();
        event2.setName("Event 2");

        when(eventRepository.findAll()).thenReturn(Arrays.asList(event1, event2));

        // When
        Optional<List<Event>> events = eventService.getEvent();

        // Then
        Assertions.assertThat(events).isPresent();
        Assertions.assertThat(events.get()).hasSize(2);
        Assertions.assertThat(events.get()).extracting(Event::getName).contains("Event 1", "Event 2");
    }

    @Test
    public void testGetEventById() {
        // Given
        Event event = new Event();
        event.setId(1L);
        event.setName("Event 1");

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        // When
        Optional<Event> foundEvent = eventService.getEventById(1L);

        // Then
        Assertions.assertThat(foundEvent).isPresent();
        Assertions.assertThat(foundEvent.get().getName()).isEqualTo("Event 1");
    }

    @Test
    public void testSaveEvent() {
        // Given
        Event event = new Event();
        event.setName("New Event");

        when(eventRepository.save(event)).thenReturn(event);

        // When
        Optional<Event> savedEvent = eventService.saveEvent(event);

        // Then
        Assertions.assertThat(savedEvent).isPresent();
        Assertions.assertThat(savedEvent.get().getName()).isEqualTo("New Event");
    }

    @Test
    public void testEditEvent() {
        // Given
        Event existingEvent = new Event();
        existingEvent.setId(1L);
        existingEvent.setName("Event 1");
        existingEvent.setDescription("Original Description");
        existingEvent.setStartDate(LocalDateTime.now());
        existingEvent.setEndDate(LocalDateTime.now().plusDays(1));

        Event updatedEvent = new Event();
        updatedEvent.setId(1L);
        updatedEvent.setName("Updated Event");
        updatedEvent.setDescription("Updated Description");
        updatedEvent.setStartDate(LocalDateTime.now().plusDays(1));
        updatedEvent.setEndDate(LocalDateTime.now().plusDays(2));

        when(eventRepository.findById(1L)).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(existingEvent)).thenReturn(updatedEvent);

        // When
        Optional<Event> result = eventService.editEvent(updatedEvent);

        // Then
        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getName()).isEqualTo("Updated Event");
        Assertions.assertThat(result.get().getDescription()).isEqualTo("Updated Description");
    }

    @Test
    public void testDeleteEvent() {
        // Given
        Long eventId = 1L;

        // When
        eventService.deleteEvent(eventId);

        // Then
        verify(eventRepository, times(1)).deleteById(eventId);
    }

    @Test
    public void testFindByMonth() {
        // Given
        Event event1 = new Event();
        event1.setStartDate(LocalDateTime.of(2024, 5, 15, 10, 0));

        Event event2 = new Event();
        event2.setStartDate(LocalDateTime.of(2024, 5, 20, 10, 0));

        when(eventRepository.findAll()).thenReturn(Arrays.asList(event1, event2));

        // When
        Optional<List<Event>> eventsInMay = eventService.findByMonth(2024, 5);

        // Then
        Assertions.assertThat(eventsInMay).isPresent();
        Assertions.assertThat(eventsInMay.get()).hasSize(2);
    }

    @Test
    public void testGetEventsForDate() {
        // Given
        LocalDate date = LocalDate.of(2024, 5, 15);
        Event event1 = new Event();
        event1.setStartDate(LocalDateTime.of(2024, 5, 10, 10, 0));
        event1.setEndDate(LocalDateTime.of(2024, 5, 20, 10, 0));

        Event event2 = new Event();
        event2.setStartDate(LocalDateTime.of(2024, 6, 10, 10, 0));
        event2.setEndDate(LocalDateTime.of(2024, 6, 15, 10, 0));

        List<Event> events = Arrays.asList(event1, event2);

        // When
        Optional<List<Event>> eventsOnDate = eventService.getEventsForDate(date, events);

        // Then
        Assertions.assertThat(eventsOnDate).isPresent();
        Assertions.assertThat(eventsOnDate.get()).hasSize(1);
        Assertions.assertThat(eventsOnDate.get().get(0).getStartDate()).isEqualTo(LocalDateTime.of(2024, 5, 10, 10, 0));
    }
}
