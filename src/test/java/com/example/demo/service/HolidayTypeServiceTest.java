package com.example.demo.service;

import com.example.demo.entity.HolidayType;
import com.example.demo.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class HolidayTypeServiceTest {

    private HolidayTypeService holidayTypeService;

    @BeforeEach
    public void setUp() {
        holidayTypeService = new HolidayTypeService();
    }

    @Test
    public void testSaveHolidayType() {
        HolidayType holidayType = new HolidayType(null, "Public Holiday", "National holiday");
        Optional<HolidayType> savedHolidayType = holidayTypeService.saveHolidayType(holidayType);

        assertTrue(savedHolidayType.isPresent());
        assertNotNull(savedHolidayType.get().getId());
        assertEquals("Public Holiday", savedHolidayType.get().getName());
        assertEquals("National holiday", savedHolidayType.get().getDescription());
        assertEquals(1, holidayTypeService.getholidayTypes().size());
    }

    @Test
    public void testGetHolidayTypeByIdSuccess() {
        HolidayType holidayType = new HolidayType(null, "Public Holiday", "National holiday");
        holidayTypeService.saveHolidayType(holidayType);

        Optional<HolidayType> retrievedHolidayType = holidayTypeService.getHolidayTypeById(1L);
        assertTrue(retrievedHolidayType.isPresent());
        assertEquals("Public Holiday", retrievedHolidayType.get().getName());
    }

    @Test
    public void testGetHolidayTypeByIdNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> {
            holidayTypeService.getHolidayTypeById(1L);
        });
    }

    @Test
    public void testEditHolidayTypeSuccess() {
        HolidayType holidayType = new HolidayType(null, "Public Holiday", "National holiday");
        holidayTypeService.saveHolidayType(holidayType);

        holidayType.setName("Updated Holiday");
        holidayType.setDescription("Updated description");
        Optional<HolidayType> updatedHolidayType = holidayTypeService.editHolidayType(holidayType);

        assertTrue(updatedHolidayType.isPresent());
        assertEquals("Updated Holiday", updatedHolidayType.get().getName());
        assertEquals("Updated description", updatedHolidayType.get().getDescription());
    }

    @Test
    public void testGetHolidayTypes() {
        HolidayType holidayType1 = new HolidayType(null, "Public Holiday", "National holiday");
        HolidayType holidayType2 = new HolidayType(null, "Bank Holiday", "Bank closed");

        holidayTypeService.saveHolidayType(holidayType1);
        holidayTypeService.saveHolidayType(holidayType2);

        List<HolidayType> holidayTypes = holidayTypeService.getholidayTypes();
        assertEquals(2, holidayTypes.size());
    }

    @Test
    public void testDeleteHolidayTypeSuccess() {
        HolidayType holidayType = new HolidayType(null, "Public Holiday", "National holiday");
        holidayTypeService.saveHolidayType(holidayType);

        holidayTypeService.deleteHolidayType(1L);
        assertEquals(0, holidayTypeService.getholidayTypes().size());
    }

    @Test
    public void testDeleteHolidayTypeNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> {
            holidayTypeService.deleteHolidayType(1L);
        });
    }

    @Test
    public void testDeleteHolidayTypeWithNonExistentId() {
        HolidayType holidayType = new HolidayType(null, "Public Holiday", "National holiday");
        holidayTypeService.saveHolidayType(holidayType);

        assertThrows(ResourceNotFoundException.class, () -> {
            holidayTypeService.deleteHolidayType(2L);
        });
    }
}
