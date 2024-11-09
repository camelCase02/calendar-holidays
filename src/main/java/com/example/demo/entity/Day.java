package com.example.demo.entity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Day {
    private LocalDate date;
    private DayOfWeek dayOfWeek;
    private int dayOfMonth;
    private List<Holiday> holidays;
    private List<Event> events;
}
