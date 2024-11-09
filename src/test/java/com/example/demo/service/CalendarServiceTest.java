package com.example.demo.service;

import com.example.demo.entity.Day;
import com.example.demo.entity.Event;
import com.example.demo.entity.Holiday;
import com.example.demo.entity.HolidayType;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CalendarServiceTest {

    @Mock
    private EventService eventService;

    @Mock
    private HolidayService holidayService;

    @Mock
    private HolidayTypeService holidayTypeService;

    @InjectMocks
    private CalendarService calendarService;

    private List<Event> eventsForMonth;
    private List<Holiday> holidaysForMonth;

    @BeforeEach
    void setUp() {
        // Initialize sample events and holidays for testing
        Event event = new Event();
        event.setName("Sample Event");
        event.setStartDate(LocalDateTime.of(2024, 5, 15, 10, 0));
        event.setEndDate(LocalDateTime.of(2024, 5, 15, 10, 0));
        eventsForMonth = List.of(event);

        Holiday holiday = new Holiday();
        holiday.setName("Sample Holiday");
        holidaysForMonth = List.of(holiday);
    }

    @Test
    public void testGetCalendar() {
        // Given
        int year = 2024;
        int month = 5;

        when(eventService.findByMonth(year, month)).thenReturn(Optional.of(eventsForMonth));
        when(holidayService.findByMonth(year, month)).thenReturn(holidaysForMonth);

        // When
        Optional<List<List<Day>>> calendar = calendarService.getCalendar(year, month);

        // Then
        Assertions.assertThat(calendar).isPresent();
        Assertions.assertThat(calendar.get()).isNotEmpty();
        Assertions.assertThat(calendar.get().size()).isGreaterThan(0);

        // Verify the days and their events and holidays
        Day day = calendar.get().get(0).get(0); // Get the first day of the calendar
        Assertions.assertThat(day.getDate()).isEqualTo(LocalDate.of(2024, 5, 1)); // Check if it's the first day of the
                                                                                  // month
        Assertions.assertThat(day.getHolidays()).contains(holidaysForMonth.get(0)); // Holiday should be present
        Assertions.assertThat(day.getEvents()).contains(eventsForMonth.get(0)); // Event should be present
    }

    @Test
    public void testGetCalendarNoEventsOrHolidays() {
        // Given
        int year = 2024;
        int month = 5;
        when(eventService.findByMonth(year, month)).thenReturn(Optional.of(new ArrayList<>()));
        when(holidayService.findByMonth(year, month)).thenReturn(new ArrayList<>());

        // When
        Optional<List<List<Day>>> calendar = calendarService.getCalendar(year, month);

        // Then
        Assertions.assertThat(calendar).isPresent();
        Assertions.assertThat(calendar.get()).isNotEmpty(); // The calendar should still be generated even if no
                                                            // events/holidays
        Assertions.assertThat(calendar.get().get(0).get(0).getHolidays()).isEmpty(); // No holidays
        Assertions.assertThat(calendar.get().get(0).get(0).getEvents()).isEmpty(); // No events
    }

    @Test
    public void testGetCalendarEdgeCaseSmallMonth() {
        int year = 2024;
        int month = 2;
        when(eventService.findByMonth(year, month)).thenReturn(Optional.of(eventsForMonth));
        when(holidayService.findByMonth(year, month)).thenReturn(holidaysForMonth);

        // When
        Optional<List<List<Day>>> calendar = calendarService.getCalendar(year, month);

        // Then
        Assertions.assertThat(calendar).isPresent();
        Assertions.assertThat(calendar.get()).isNotEmpty();
        Assertions.assertThat(calendar.get().size()).isGreaterThan(0);
    }

    @Test
    public void testGetHolidayTypes() {
        List<HolidayType> holidayTypes = List.of(new HolidayType(1L, "National", "National Holidays"),
                new HolidayType(2L, "Religious", "Religious Holidays"));
        when(holidayTypeService.getholidayTypes()).thenReturn(holidayTypes);

    
        List<HolidayType> result = calendarService.getHolidayTypes();

    
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result).hasSize(2);
        Assertions.assertThat(result.get(0).getName()).isEqualTo("National");
        Assertions.assertThat(result.get(1).getName()).isEqualTo("Religious");
    }
}
