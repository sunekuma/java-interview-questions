package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.controller.model.HolidayResponse;
import com.example.demo.service.HolidayService;

import lombok.SneakyThrows;

@ExtendWith(MockitoExtension.class)
class HolidayControllerTest {

	@Mock
    private HolidayService holidayService;

    @InjectMocks
    private HolidayController holidayController;
    
    @SneakyThrows
    @BeforeEach
    void init() {
        Mockito.when(holidayService.getNearestHoliday(Mockito.anyString(), Mockito.anyString())).thenReturn(HolidayResponse.builder().holidayName("test").nearestHolidayDate(LocalDate.now()).build());
    }

    @SneakyThrows
    @Test
    public void testNearestHoliday() {
        HolidayResponse response = holidayController.nearestHoliday("CA", "2022-08-30").getBody();
        assertEquals("test", response.getHolidayName());
    }

}
