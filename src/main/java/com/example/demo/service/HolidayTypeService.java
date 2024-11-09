package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.HolidayType;
import com.example.demo.exception.ResourceNotFoundException;

@Service
public class HolidayTypeService {

    private List<HolidayType> holidayTypeList = new ArrayList<>();

    public List<HolidayType> getholidayTypes() {

        return holidayTypeList;
    }

    public Optional<HolidayType> getHolidayTypeById(Long id) {
        if (id.intValue() > holidayTypeList.size()) {
            throw new ResourceNotFoundException(
                    "Holiday Type not found with id " + id + ". Total number of holiday types is "
                            + holidayTypeList.size());
        }

        for (HolidayType holidayType : holidayTypeList) {
            if (holidayType.getId().equals(id)) {
                return Optional.of(holidayType);
            }
        }
        return Optional.empty();
    }

    public Optional<HolidayType> saveHolidayType(HolidayType holidayType) {
        holidayType.setId(Long.valueOf(holidayTypeList.size()) + 1);
        holidayTypeList.add(holidayType);
        return Optional.of(holidayType);
    }

    public Optional<HolidayType> editHolidayType(HolidayType holidayType) {
        for (int i = 0; i < holidayTypeList.size(); i++) {
            if (holidayTypeList.get(i).getId().equals(holidayType.getId())) {
                holidayTypeList.set(i, holidayType);
            }
        }
        return Optional.of(holidayType);
    }

    public void deleteHolidayType(Long id) {
        try {
            for (int i = 0; i < holidayTypeList.size(); i++) {
                if (holidayTypeList.get(i).getId().equals(id)) {
                    holidayTypeList.remove(i);
                    return;
                }
            }
            throw new ResourceNotFoundException("Holiday Type not found with id " + id);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Holiday Type not found with id " + id);
        }
    }
}
