package com.example.demo.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

import com.example.demo.controller.model.HolidayDto;
import com.example.demo.controller.model.HolidayResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.SneakyThrows;

@ExtendWith(MockitoExtension.class)
class HolidayModelConverterTest {
	
	@Spy
	private ObjectMapper objectMapper;
	
	private String data;
	
	@InjectMocks
	private HolidayModelConverter holidayModelConverter;
	
	@SneakyThrows
	@BeforeEach
	public void init() {
		objectMapper.registerModule(new JavaTimeModule());
		File resource = new ClassPathResource("response.json").getFile();
		this.data = new String(Files.readAllBytes(resource.toPath()));

	}

	@SneakyThrows
	@Test
	void testConvertToHolidayDtoType() {
		HolidayDto holidayData = holidayModelConverter.convertToHolidayDtoType(data);
		assertEquals("200", holidayData.getStatus());
		assertEquals(33, holidayData.getHolidays().size());
	}
	
	@SneakyThrows
	@Test
	void testConvertToHolidayResponseType_with_holiday() {
		HolidayDto holidayData = holidayModelConverter.convertToHolidayDtoType(data);
		HolidayResponse holidayResponse = holidayModelConverter.convertToHolidayResponseType(holidayData, LocalDate.of(2021, 2, 2));
		assertEquals(LocalDate.of(2021, 2, 14), holidayResponse.getNearestHolidayDate());
		assertEquals("Valentine's Day", holidayResponse.getHolidayName());
	}
	
	@SneakyThrows
	@Test
	void testConvertToHolidayResponseType_without_holiday() {
		HolidayDto holidayData = holidayModelConverter.convertToHolidayDtoType(data);
		HolidayResponse holidayResponse = holidayModelConverter.convertToHolidayResponseType(holidayData, LocalDate.of(2022, 2, 2));
		assertEquals(LocalDate.of(2021, 12, 31), holidayResponse.getNearestHolidayDate());
		assertEquals("New Year's Eve", holidayResponse.getHolidayName());
	}

}
