package com.example.demo.service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.controller.model.HolidayDto;
import com.example.demo.controller.model.HolidayResponse;
import com.example.demo.persistence.HolidayEntity;
import com.example.demo.repository.HolidayRespository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class HolidayService {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private HolidayApiConsumer holidayApiConsumer;

	@Autowired
	private HolidayRespository holidayRespository;

	public HolidayResponse getNearestHoliday(final String countryCode, final String date)
			throws JsonMappingException, JsonProcessingException {
		LocalDate searchDate = null;
		LocalDate inputDate = null;
		HolidayDto holidayData = null;
		// find input date is null. if yes, read the current date
		if (date == null) {
			searchDate = LocalDate.now().minusYears(1);
		} else {
			searchDate = LocalDate.now().minusYears(1);
			inputDate = LocalDate.parse(date);
		}
		// check if the holiday data is already present in h2 database
		// checking the data for only 2021, since HolidayAPI free subscription not
		// returning any data other than last year
		Optional<HolidayEntity> holidayEntityData = holidayRespository.findById(searchDate.getYear());
		if (holidayEntityData.isPresent()) {
			holidayData = convertToHolidayDtoType(holidayEntityData.get().getData());
			// send the response in required format
			return buildResponse(holidayData, inputDate);
		}

		// fetch data from HolidayAPI
		String responseData = holidayApiConsumer.retrieveHolidays(countryCode, searchDate);

		// persist the data in h2 database
		holidayRespository.save(HolidayEntity.builder().year(searchDate.getYear()).data(responseData).build());
		holidayData = convertToHolidayDtoType(responseData);

		// send the response in required format
		return buildResponse(holidayData, inputDate);
	}

	private HolidayResponse buildResponse(HolidayDto holidayData, LocalDate inputDate) {
		Map<LocalDate, String> holidayMap = new TreeMap();
		HolidayResponse response = null;
		holidayData.getHolidays().forEach(holiday -> holidayMap.put(holiday.getDate(), holiday.getName()));
		for (Entry<LocalDate, String> holidayEntry : holidayMap.entrySet()) {
			if (inputDate.isBefore(holidayEntry.getKey())) {
				response = HolidayResponse.builder().holidayName(holidayEntry.getValue())
						.nearestHolidayDate(holidayEntry.getKey()).build();
			}
		}
		if (response == null) {
			response = HolidayResponse.builder()
					.holidayName(holidayData.getHolidays().get(holidayMap.size() - 1).getName())
					.nearestHolidayDate(holidayData.getHolidays().get(holidayMap.size() - 1).getDate()).build();
		}
		return response;
	}

	private HolidayDto convertToHolidayDtoType(final String data) throws JsonMappingException, JsonProcessingException {
		return objectMapper.readValue(data, new TypeReference<HolidayDto>() {
		});
	}

}
