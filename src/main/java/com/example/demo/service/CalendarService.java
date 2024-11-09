package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Day;
import com.example.demo.entity.Event;
import com.example.demo.entity.Holiday;
import com.example.demo.entity.HolidayType;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CalendarService {

    @Autowired
    private EventService eventService;
    @Autowired
    private HolidayService holidayService;

    @Autowired
    private HolidayTypeService holidayTypeService;

    public Optional<List<List<Day>>> getCalendar(int year, int month) {
        List<List<Day>> weeks = new ArrayList<>();

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstOfMonth = yearMonth.atDay(1);
        int daysInMonth = yearMonth.lengthOfMonth();

        // Start from the first day of the month
        LocalDate currentDay = firstOfMonth;

        // Find events and holidays for the entire month
        List<Holiday> holidaysForMonth = holidayService.findByMonth(year, month);

        List<Event> eventsForMonth = eventService.findByMonth(year, month).get();

        // Loop through the month and create weeks
        List<Day> week = new ArrayList<>();
        for (int i = 1; i <= daysInMonth; i++) {
            LocalDate date = currentDay.withDayOfMonth(i);
            List<Holiday> holidays = holidayService.getHolidaysForDate(date, holidaysForMonth);
            List<Event> events = eventService.getEventsForDate(date, eventsForMonth).get();

            // Create the Day object and add it to the current week
            Day day = new Day(date, date.getDayOfWeek(), i, holidays, events);
            week.add(day);

            // If the week is full (7 days) or it's the end of the month, add the week to
            // the list and start a new week
            if (date.getDayOfWeek().getValue() % 7 == 0 || i == daysInMonth) {
                weeks.add(week);
                week = new ArrayList<>();
            }
        }
        return Optional.of(weeks);
    }

    public List<HolidayType> getHolidayTypes() {
        return holidayTypeService.getholidayTypes();
    }
}
