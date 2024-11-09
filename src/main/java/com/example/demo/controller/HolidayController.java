package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Holiday;
import com.example.demo.entity.HolidayType;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.HolidayService;
import com.example.demo.service.HolidayTypeService;

@RestController
public class HolidayController {

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private HolidayTypeService holidayTypeService;

    @GetMapping("/holidays")
    public List<Holiday> getHolidays() {
        return holidayService.getHolidays()
                .orElseThrow(() -> new ResourceNotFoundException("Error fetching All Holidays"));
    }

    @GetMapping("/holiday/{id}")
    public Holiday getHolidayById(@PathVariable(name = "id") Long id) {
        if (id <= 0) {
            throw new InvalidInputException("Id must be greater than 0");
        }

        return holidayService.getHolidayById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday not found with id " + id));
    }

    @PostMapping("/holiday")
    public Holiday saveHoliday(@RequestBody Holiday holiday) {
        return holidayService.saveHoliday(holiday)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday not saved"));
    }

    @PutMapping("/holiday")
    public Holiday updateHoliday(@RequestBody Holiday holiday) {
        return holidayService.editHoliday(holiday)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday not updated"));
    }

    @DeleteMapping("/holiday/{id}")
    public void deleteHoliday(@PathVariable(name = "id") Long id) {
        if (id <= 0) {
            throw new InvalidInputException("Id must be greater than 0");
        }
        try {
            holidayService.deleteHoliday(id);
        } catch (Exception e) {
            throw e;
        }
    }

    @PostMapping("/holidaytype")
    public HolidayType saveHolidayType(@RequestBody HolidayType holidayType) {
        try {
            return holidayTypeService.saveHolidayType(holidayType)
                    .orElseThrow(() -> new ResourceNotFoundException("Holiday Type not saved"));
        } catch (Exception e) {
            throw e;
        }
    }
}
