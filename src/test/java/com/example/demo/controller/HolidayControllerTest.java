package com.example.demo.controller;

import com.example.demo.entity.Holiday;
import com.example.demo.entity.HolidayType;
import com.example.demo.service.HolidayService;
import com.example.demo.service.HolidayTypeService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

@WebMvcTest(HolidayController.class)
public class HolidayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HolidayService holidayService;

    @MockBean
    private HolidayTypeService holidayTypeService;

    @InjectMocks
    private HolidayController holidayController;

    @Test
    public void testGetHolidays_validRequest() throws Exception {
        // Arrange
        Holiday holiday1 = new Holiday(1L, "New Year", "New Year's Day",
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 1, 1, 23, 59),
                null); // Assuming HolidayType is not used here
        Holiday holiday2 = new Holiday(2L, "Christmas", "Christmas Day",
                LocalDateTime.of(2024, 12, 25, 0, 0),
                LocalDateTime.of(2024, 12, 25, 23, 59),
                null);
        List<Holiday> holidays = Arrays.asList(holiday1, holiday2);

        when(holidayService.getHolidays()).thenReturn(Optional.of(holidays));

        // Act & Assert
        mockMvc.perform(get("/holidays"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].name").value("Christmas"));
    }

    @Test
    public void testGetHolidayById_validRequest() throws Exception {
        // Arrange
        Holiday holiday = new Holiday(1L, "New Year", "New Year's Day",
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 1, 1, 23, 59),
                null);
                
        when(holidayService.getHolidayById(1L)).thenReturn(java.util.Optional.of(holiday));

        // Act & Assert
        mockMvc.perform(get("/holiday/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("New Year"));
    }

    @Test
    public void testGetHolidayById_invalidId() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/holiday/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Holiday not found with id 999"));
    }

    @Test
    public void testSaveHoliday_validRequest() throws Exception {
        // Arrange
        Holiday holiday = new Holiday(null, "Labor Day", "Labor Day Holiday",
                LocalDateTime.of(2024, 5, 1, 0, 0),
                LocalDateTime.of(2024, 5, 1, 23, 59),
                null);
        when(holidayService.saveHoliday(any(Holiday.class))).thenReturn(java.util.Optional.of(holiday));

        // Act & Assert
        mockMvc.perform(post("/holiday")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"name\":\"Labor Day\",\"description\":\"Labor Day Holiday\",\"startDate\":\"2024-05-01T00:00:00\",\"endDate\":\"2024-05-01T23:59:59\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Labor Day"));
    }

    @Test
    public void testDeleteHoliday_validRequest() throws Exception {
        // Arrange
        doNothing().when(holidayService).deleteHoliday(1L);

        // Act & Assert
        mockMvc.perform(delete("/holiday/1"))
                .andExpect(status().isOk());

        // Verify interaction with service
        verify(holidayService, times(1)).deleteHoliday(1L);
    }

    @Test
    public void testDeleteHoliday_invalidId() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Holiday not found with id 999")).when(holidayService).deleteHoliday(999L);

        // Act & Assert
        mockMvc.perform(delete("/holiday/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Holiday not found with id 999"));
    }

    @Test
    public void testSaveHolidayType_validRequest() throws Exception {
        // Arrange
        HolidayType holidayType = new HolidayType(null, "Public Holiday", "A national holiday");
        when(holidayTypeService.saveHolidayType(any(HolidayType.class))).thenReturn(java.util.Optional.of(holidayType));

        // Act & Assert
        mockMvc.perform(post("/holidaytype")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Public Holiday\",\"description\":\"A national holiday\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Public Holiday"));
    }

    @Test
    public void testInvalidHolidayId() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/holiday/0")) // Invalid ID should trigger exception
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Id must be greater than 0"));
    }
}
