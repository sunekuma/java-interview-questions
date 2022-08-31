package com.example.demo.converter;

import java.time.LocalDate;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import com.example.demo.controller.model.HolidayDto;
import com.example.demo.controller.model.HolidayResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class HolidayModelConverter {
	
	private ObjectMapper objectMapper;	
	
	public HolidayModelConverter(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public HolidayResponse convertToHolidayResponseType(HolidayDto holidayData, LocalDate inputDate) {
		Map<LocalDate, String> holidayMap = new TreeMap<>();
		HolidayResponse response = null;
		holidayData.getHolidays().forEach(holiday -> holidayMap.put(holiday.getDate(), holiday.getName()));
		for (Entry<LocalDate, String> holidayEntry : holidayMap.entrySet()) {
			if (inputDate.isBefore(holidayEntry.getKey())) {
				response = HolidayResponse.builder().holidayName(holidayEntry.getValue())
						.nearestHolidayDate(holidayEntry.getKey()).build();
				break;
			}
		}
		if (response == null) {
			response = HolidayResponse.builder()
					.holidayName(holidayData.getHolidays().get(holidayMap.size() - 1).getName())
					.nearestHolidayDate(holidayData.getHolidays().get(holidayMap.size() - 1).getDate()).build();
		}
		return response;
	}

	public HolidayDto convertToHolidayDtoType(final String data) throws JsonMappingException, JsonProcessingException {
		return objectMapper.readValue(data, new TypeReference<HolidayDto>() {
		});
	}

}
