package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

import com.example.demo.controller.model.HolidayResponse;
import com.example.demo.converter.HolidayModelConverter;
import com.example.demo.persistence.HolidayEntity;
import com.example.demo.repository.HolidayRespository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.SneakyThrows;

@ExtendWith(MockitoExtension.class)
class HolidayServiceTest {
	
	@Mock
	private HolidayApiConsumer holidayApiConsumer;

	@Spy
	private HolidayRespository holidayRespository;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@InjectMocks
	private HolidayService holidayService;
	
	@Spy
	private HolidayModelConverter holidayModelConverter = new HolidayModelConverter(this.objectMapper);;
	
	private String data;
	
	@SneakyThrows
	@BeforeEach
	public void init() {
		objectMapper.registerModule(new JavaTimeModule());
		File resource = new ClassPathResource("response.json").getFile();
		this.data = new String(Files.readAllBytes(resource.toPath()));
	}

	private Optional<HolidayEntity> mockHolidayEntity() {
		HolidayEntity holidayEntity =  HolidayEntity.builder().year(2021).data(this.data).build();
		return Optional.of(holidayEntity);
	}

	@SneakyThrows
	@Test
	void testGetNearestHoliday_Data_DB() {
        Mockito.doReturn(mockHolidayEntity()).when(holidayRespository).findHolidaysByCountryAndYear(Mockito.anyString(), Mockito.anyInt());
		HolidayResponse holidayResponse = holidayService.getNearestHoliday("CA", "2021-01-17");
		assertEquals(LocalDate.of(2021, 2, 2), holidayResponse.getNearestHolidayDate());
		assertEquals("Groundhog Day", holidayResponse.getHolidayName());
	}
	
	@SneakyThrows
	@Test
	void testGetNearestHoliday_Date_Null() {
        Mockito.doReturn(mockHolidayEntity()).when(holidayRespository).findHolidaysByCountryAndYear(Mockito.anyString(), Mockito.anyInt());
		HolidayResponse holidayResponse = holidayService.getNearestHoliday("CA", null);
		assertEquals(LocalDate.of(2021, 12, 31), holidayResponse.getNearestHolidayDate());
		assertEquals("New Year's Eve", holidayResponse.getHolidayName());
	}
	
	@SneakyThrows
	@Test
	void testGetNearestHoliday_Data_API() {
        Mockito.doReturn(this.data).when(holidayApiConsumer).retrieveHolidays(Mockito.anyString(), Mockito.any(LocalDate.class));
        Mockito.doReturn(mockHolidayEntity().get()).when(this.holidayRespository).save(Mockito.any(HolidayEntity.class));
		HolidayResponse holidayResponse = holidayService.getNearestHoliday("CA", "2021-02-14");
		assertEquals(LocalDate.of(2021, 2, 15), holidayResponse.getNearestHolidayDate());
		assertEquals("National Flag of Canada Day", holidayResponse.getHolidayName());
	}

}
