package com.example.demo.service;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HolidayApiConsumer {
	
	@Autowired
	private RestTemplate restTemplate;
	
    private static final String HOLIDAY_API_URL = "https://holidayapi.com/v1/holidays";
	
	@Value("${holiday.api.key:#null}")
    private String holidayApiKey;
	
	public String retrieveHolidays(final String countryCode, final LocalDate searchDate) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity <String> requestEntity = new HttpEntity<String>(headers);
		StringBuilder url = new StringBuilder(HOLIDAY_API_URL).append("?pretty&key=").append(holidayApiKey).append("&country=").append(countryCode).append("&year=").append(searchDate.getYear());
		ResponseEntity<String> holidayDataResponse = restTemplate.exchange(url.toString(), HttpMethod.GET, requestEntity, String.class);
		return holidayDataResponse.getBody();
	}
}
