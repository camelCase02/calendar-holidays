package com.example.demo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Holiday;
import com.example.demo.exception.ResourceNotFoundException;

@Service
public class HolidayService {

    private List<Holiday> holidaysList = new ArrayList<>();

    public Optional<List<Holiday>> getHolidays() {
        return Optional.of(holidaysList);
    }

    public Optional<Holiday> getHolidayById(Long id) {
        System.out.println("id: " + id);
        if (id.intValue() > holidaysList.size()) {
            throw new ResourceNotFoundException(
                    "Holiday not found with id " + id + ". Total number of holidays is " + holidaysList.size());
        }

        for (Holiday holiday : holidaysList) {
            if (holiday.getId().equals(id)) {
                return Optional.of(holiday);
            }
        }
        throw new ResourceNotFoundException("Holiday not found with id " + id);
    }

    public Optional<Holiday> saveHoliday(Holiday holiday) {
        holiday.setId(Long.valueOf(holidaysList.size()) + 1);
        holidaysList.add(holiday);
        return Optional.of(holiday);
    }

    public Optional<Holiday> editHoliday(Holiday holiday) {
        for (int i = 0; i < holidaysList.size(); i++) {
            if (holidaysList.get(i).getId().equals(holiday.getId())) {
                holidaysList.set(i, holiday);
            }
        }
        return Optional.of(holiday);
    }

    public void deleteHoliday(Long id) {
        try {
            for (int i = 0; i < holidaysList.size(); i++) {
                if (holidaysList.get(i).getId().equals(id)) {
                    holidaysList.remove(i);
                    return;
                }
            }
            throw new ResourceNotFoundException("Holiday not found with id " + id);
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Holiday> findByMonth(int year, int month) {
        List<Holiday> holidaysForMonth = new ArrayList<>();
        for (Holiday holiday : holidaysList) {
            if (holiday.getStartDate().getYear() == year && holiday.getStartDate().getMonthValue() == month) {
                holidaysForMonth.add(holiday);
            }
        }

        return holidaysForMonth;
    }

    public List<Holiday> getHolidaysForDate(LocalDate date, List<Holiday> holidays) {
        List<Holiday> holidaysOnDate = new ArrayList<>();
        for (Holiday holiday : holidays) {
            if (!date.isBefore(holiday.getStartDate().toLocalDate())
                    && !date.isAfter(holiday.getEndDate().toLocalDate())) {
                holidaysOnDate.add(holiday);
            }
        }
        return holidaysOnDate;
    }
}
