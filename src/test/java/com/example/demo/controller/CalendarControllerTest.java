package com.example.demo.controller;

import com.example.demo.service.CalendarService;
import com.example.demo.entity.Day;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CalendarController.class)
public class CalendarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalendarService calendarService;

    @InjectMocks
    private CalendarController calendarController;

    @Test
    public void testGetCalendar_validRequest() throws Exception {
        List<List<Day>> weeks = new ArrayList<>();
        when(calendarService.getCalendar(anyInt(), anyInt())).thenReturn(Optional.of(weeks));

        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(calendarService, times(1)).getCalendar(2024, 11);
    }

    @Test
    public void testGetCalendar_invalidMonth() throws Exception {
        mockMvc.perform(get("/home")
                .param("year", "2024")
                .param("month", "13"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Month must be between 1 and 12."));
    }

    @Test
    public void testGetCalendar_invalidYear() throws Exception {
        mockMvc.perform(get("/home")
                .param("year", "1700") // Invalid year
                .param("month", "11"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("year must be between 1800 to 2100"));
    }
}
