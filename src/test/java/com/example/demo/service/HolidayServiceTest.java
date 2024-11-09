package com.example.demo.service;

import com.example.demo.entity.Holiday;
import com.example.demo.entity.HolidayType;
import com.example.demo.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class HolidayServiceTest {

    private HolidayService holidayService;

    @BeforeEach
    void setUp() {
        holidayService = new HolidayService();
    }

    private Holiday createHoliday(Long id, String name, LocalDateTime startDate, LocalDateTime endDate,
            HolidayType holidayType) {
        return new Holiday(id, name, "Description", startDate, endDate, holidayType);
    }

    private HolidayType createHolidayType(Long id, String name) {
        return new HolidayType(id, name, "Description for " + name);
    }

    @Test
    void testGetHolidays() {
        // Given
        HolidayType holidayType = createHolidayType(1L, "Public Holiday");
        Holiday holiday = createHoliday(1L, "Sample Holiday", LocalDateTime.of(2024, Month.MAY, 1, 10, 0),
                LocalDateTime.of(2024, Month.MAY, 1, 18, 0), holidayType);
        holidayService.saveHoliday(holiday);

        // When
        Optional<List<Holiday>> holidays = holidayService.getHolidays();

        // Then
        assertThat(holidays).isPresent();
        assertThat(holidays.get()).isNotEmpty();
        assertThat(holidays.get()).hasSize(1);
        assertThat(holidays.get().get(0).getName()).isEqualTo("Sample Holiday");
    }

    @Test
    void testGetHolidayById() {
        // Given
        HolidayType holidayType = createHolidayType(1L, "Public Holiday");
        Holiday holiday = createHoliday(1L, "Sample Holiday", LocalDateTime.of(2024, Month.MAY, 1, 10, 0),
                LocalDateTime.of(2024, Month.MAY, 1, 18, 0), holidayType);
        holidayService.saveHoliday(holiday);

        // When
        Optional<Holiday> result = holidayService.getHolidayById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Sample Holiday");
    }

    @Test
    void testSaveHoliday() {
        // Given
        HolidayType holidayType = createHolidayType(1L, "Public Holiday");
        Holiday holiday = createHoliday(null, "National Day", LocalDateTime.of(2024, Month.AUGUST, 15, 0, 0),
                LocalDateTime.of(2024, Month.AUGUST, 15, 23, 59), holidayType);

        // When
        Optional<Holiday> savedHoliday = holidayService.saveHoliday(holiday);

        // Then
        assertThat(savedHoliday).isPresent();
        assertThat(savedHoliday.get().getName()).isEqualTo("National Day");
        assertThat(savedHoliday.get().getHolidayType().getName()).isEqualTo("Public Holiday");
        assertThat(savedHoliday.get().getId()).isNotNull();
    }

    @Test
    void testEditHoliday() {
        // Given
        HolidayType holidayType = createHolidayType(1L, "Public Holiday");
        Holiday holiday = createHoliday(1L, "National Day", LocalDateTime.of(2024, Month.AUGUST, 15, 0, 0),
                LocalDateTime.of(2024, Month.AUGUST, 15, 23, 59), holidayType);
        holidayService.saveHoliday(holiday);

        Holiday updatedHoliday = new Holiday(1L, "Updated National Day", "Updated description",
                LocalDateTime.of(2024, Month.AUGUST, 15, 0, 0), LocalDateTime.of(2024, Month.AUGUST, 15, 23, 59),
                holidayType);

        // When
        Optional<Holiday> result = holidayService.editHoliday(updatedHoliday);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Updated National Day");
        assertThat(result.get().getDescription()).isEqualTo("Updated description");
    }

    @Test
    void testDeleteHoliday() {
        // Given
        HolidayType holidayType = createHolidayType(1L, "Public Holiday");
        Holiday holiday = createHoliday(1L, "National Day", LocalDateTime.of(2024, Month.AUGUST, 15, 0, 0),
                LocalDateTime.of(2024, Month.AUGUST, 15, 23, 59), holidayType);
        holidayService.saveHoliday(holiday);

        // When
        holidayService.deleteHoliday(1L);

        // Then
        assertThat(holidayService.getHolidays().get()).isEmpty();
    }

    @Test
    void testDeleteHolidayThrowsException() {
        // Given: No holidays in the list
        Long nonExistingHolidayId = 999L;

        // When & Then
        assertThatThrownBy(() -> holidayService.deleteHoliday(nonExistingHolidayId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Holiday not found with id " + nonExistingHolidayId);
    }

    @Test
    void testFindByMonth() {
        // Given
        HolidayType holidayType = createHolidayType(1L, "Public Holiday");
        Holiday holiday1 = createHoliday(1L, "New Year's Day", LocalDateTime.of(2024, Month.JANUARY, 1, 10, 0),
                LocalDateTime.of(2024, Month.JANUARY, 1, 10, 0), holidayType);
        Holiday holiday2 = createHoliday(2L, "Sample Holiday", LocalDateTime.of(2024, Month.MAY, 15, 10, 0),
                LocalDateTime.of(2024, Month.MAY, 15, 18, 0), holidayType);
        holidayService.saveHoliday(holiday1);
        holidayService.saveHoliday(holiday2);

        // When
        List<Holiday> holidaysInMay = holidayService.findByMonth(2024, 5);

        // Then
        assertThat(holidaysInMay).hasSize(1);
        assertThat(holidaysInMay.get(0).getName()).isEqualTo("Sample Holiday");
    }

    @Test
    void testGetHolidaysForDate() {
        // Given
        HolidayType holidayType = createHolidayType(1L, "Public Holiday");
        Holiday holiday1 = createHoliday(1L, "National Day", LocalDateTime.of(2024, Month.AUGUST, 15, 0, 0),
                LocalDateTime.of(2024, Month.AUGUST, 15, 23, 59), holidayType);
        Holiday holiday2 = createHoliday(2L, "Labor Day", LocalDateTime.of(2024, Month.MAY, 1, 0, 0),
                LocalDateTime.of(2024, Month.MAY, 1, 23, 59), holidayType);
        holidayService.saveHoliday(holiday1);
        holidayService.saveHoliday(holiday2);

        // When
        List<Holiday> holidaysOnDate = holidayService.getHolidaysForDate(LocalDate.of(2024, Month.MAY, 1),
                holidayService.getHolidays().get());

        // Then
        assertThat(holidaysOnDate).hasSize(1);
        assertThat(holidaysOnDate.get(0).getName()).isEqualTo("Labor Day");
    }
}
