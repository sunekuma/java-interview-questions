package com.example.demo.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.controller.model.HolidayDto;
import com.example.demo.controller.model.HolidayResponse;
import com.example.demo.converter.HolidayModelConverter;
import com.example.demo.persistence.HolidayEntity;
import com.example.demo.repository.HolidayRespository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class HolidayService {

	@Autowired
	private HolidayApiConsumer holidayApiConsumer;

	@Autowired
	private HolidayRespository holidayRespository;
	
	@Autowired
	private HolidayModelConverter holidayModelConverter;

	public HolidayResponse getNearestHoliday(final String countryCode, final String date)
			throws JsonMappingException, JsonProcessingException {
		LocalDate searchDate = null;
		LocalDate inputDate = null;
		HolidayDto holidayData = null;
		// find input date is null. if yes, read the current date
		if (date == null) {
			searchDate = LocalDate.now().minusYears(1);
			inputDate = LocalDate.now();
		} else {
			searchDate = LocalDate.now().minusYears(1);
			inputDate = LocalDate.parse(date);
		}
		// check if the holiday data is already present in h2 database
		Optional<HolidayEntity> holidayEntityData = holidayRespository.findById(searchDate.getYear());
		if (holidayEntityData.isPresent()) {
			holidayData = holidayModelConverter.convertToHolidayDtoType(holidayEntityData.get().getData());
			// send the response in required format
			return holidayModelConverter.convertToHolidayResponseType(holidayData, inputDate);
		}

		// fetch data from HolidayAPI for 2021, since HolidayAPI free subscription not returning any data other than last year
		String responseData = holidayApiConsumer.retrieveHolidays(countryCode, searchDate);

		// persist the data in h2 database
		holidayRespository.save(HolidayEntity.builder().year(searchDate.getYear()).data(responseData).build());
		holidayData = holidayModelConverter.convertToHolidayDtoType(responseData);

		// send the response in required format
		return holidayModelConverter.convertToHolidayResponseType(holidayData, inputDate);
	}
}
