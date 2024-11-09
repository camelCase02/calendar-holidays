package com.example.demo.controller;

import com.example.demo.entity.Event;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.EventService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    private Event sampleEvent;

    @BeforeEach
    public void setUp() {
        sampleEvent = new Event(1L, "Sample Event", "Description", LocalDateTime.now(),
                LocalDateTime.now().plusHours(2));
    }

    @Test
    public void testGetEvents() throws Exception {
        List<Event> events = Arrays.asList(sampleEvent);
        when(eventService.getEvent()).thenReturn(Optional.of(events));

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(sampleEvent.getId()))
                .andExpect(jsonPath("$[0].name").value(sampleEvent.getName()));
    }

    @Test
    public void testGetEventsNotFound() throws Exception {
        when(eventService.getEvent()).thenReturn(Optional.empty());

        mockMvc.perform(get("/events"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetEventById_Success() throws Exception {
        when(eventService.getEventById(1L)).thenReturn(Optional.of(sampleEvent));

        mockMvc.perform(get("/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleEvent.getId()))
                .andExpect(jsonPath("$.name").value(sampleEvent.getName()));
    }

    @Test
    public void testGetEventById_InvalidId() throws Exception {
        mockMvc.perform(get("/events/0"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertThat(result.getResolvedException())
                        .isInstanceOf(InvalidInputException.class));
    }

    @Test
    public void testGetEventById_NotFound() throws Exception {
        when(eventService.getEventById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/events/1"))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertThat(result.getResolvedException())
                        .isInstanceOf(ResourceNotFoundException.class));
    }

    @Test
    public void testSaveEvent_Success() throws Exception {
        when(eventService.saveEvent(any(Event.class))).thenReturn(Optional.of(sampleEvent));

        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Sample Event\", \"description\": \"Description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleEvent.getId()))
                .andExpect(jsonPath("$.name").value(sampleEvent.getName()));
    }

    @Test
    public void testSaveEvent_Failure() throws Exception {
        when(eventService.saveEvent(any(Event.class))).thenReturn(Optional.empty());

        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Sample Event\", \"description\": \"Description\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateEvent_Success() throws Exception {
        final Event updatedEvent = sampleEvent;
        updatedEvent.setName("Updated Event");
        updatedEvent.setDescription("Updated Description");
        when(eventService.editEvent(any(Event.class))).thenReturn(Optional.of(updatedEvent));

        mockMvc.perform(put("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1, \"name\": \"Updated Event\", \"description\": \"Updated Description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleEvent.getId()))
                .andExpect(jsonPath("$.name").value("Updated Event"));
    }

    @Test
    public void testUpdateEvent_Failure() throws Exception {
        when(eventService.editEvent(any(Event.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1, \"name\": \"Updated Event\", \"description\": \"Updated Description\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteEvent_Success() throws Exception {
        doNothing().when(eventService).deleteEvent(1L);

        mockMvc.perform(delete("/events/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteEvent_InvalidId() throws Exception {
        mockMvc.perform(delete("/events/0"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertThat(result.getResolvedException())
                        .isInstanceOf(InvalidInputException.class));
    }

    @Test
    public void testDeleteEvent_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Event not found")).when(eventService).deleteEvent(1L);

        mockMvc.perform(delete("/events/1"))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertThat(result.getResolvedException())
                        .isInstanceOf(ResourceNotFoundException.class));
    }
}
