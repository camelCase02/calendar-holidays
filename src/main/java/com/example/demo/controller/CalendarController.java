package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Day;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.service.CalendarService;

import java.time.LocalDate;
import java.util.List;

@RestController
public class CalendarController {
    @Autowired
    private CalendarService calendarService;

    @GetMapping("/home")
    public List<List<Day>> getCalendar(@RequestParam(name = "month", defaultValue = "0", required = false) int month,
            @RequestParam(name = "year", defaultValue = "0", required = false) int year) {
        if (month == 0) {
            month = LocalDate.now().getMonthValue();
        }
        if (year == 0) {
            year = LocalDate.now().getYear();
        }

        if (month < 1 || month > 12) {
            throw new InvalidInputException("Month must be between 1 and 12.");
        }

        if (year < 1800 || year > 2100) {
            throw new InvalidInputException("year must be between 1800 to 2100");
        }

        try {
            List<List<Day>> weeks = calendarService.getCalendar(year, month).get();

            return weeks;
        } catch (Exception e) {
            throw e;
        }
    }
}
