package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.SneakyThrows;

@ExtendWith(MockitoExtension.class)
class HolidayApiConsumerTest {
	
	@Mock
	private RestTemplate restTemplate;
	
	@InjectMocks
	private HolidayApiConsumer holidayApiConsumer;
	
	private String data;
	
	@Spy
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@SneakyThrows
	@BeforeEach
	public void init() {
		objectMapper.registerModule(new JavaTimeModule());
		File resource = new ClassPathResource("response.json").getFile();
		this.data = new String(Files.readAllBytes(resource.toPath()));
	}

	@Test
	void testRetrieveHolidays() {
        Mockito.when(restTemplate.exchange(Mockito.any(String.class), Mockito.any(HttpMethod.class), Mockito.any(HttpEntity.class), Mockito.any(Class.class))).thenReturn(mockResponse());
        String response = holidayApiConsumer.retrieveHolidays("CA", LocalDate.of(2021, 03, 10));
        assertEquals(response, this.data);
	}

	private ResponseEntity<String> mockResponse() {
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<String> responseEntity = new ResponseEntity<>(data, header, HttpStatus.OK);
		return responseEntity;
	}

}
