package com.example.demo.controller;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.model.HolidayResponse;
import com.example.demo.service.HolidayService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * 
 * @author suneel kumar
 *
 */
@Validated
@RestController
public class HolidayController {

	@Autowired
	private HolidayService holidayService;

	@GetMapping("/holiday")
	public ResponseEntity<HolidayResponse> nearestHoliday(
			@RequestParam(name = "countryCode", required = true) @NotEmpty(message = "The countryCode is required.") @Size(min = 2, max = 2, message = "please enter 2 digit country code") String countryCode,
			@RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date)
			throws JsonMappingException, JsonProcessingException {
		return new ResponseEntity<HolidayResponse>(holidayService.getNearestHoliday(countryCode, date), HttpStatus.OK);
	}

}
