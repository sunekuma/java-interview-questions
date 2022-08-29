package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RestController
public class HolidayController {
	
	@Autowired
	private HolidayService holidayService;
	
	@GetMapping("/holiday")
	public ResponseEntity<HolidayResponse> nearestHoliday(
			@RequestParam(name = "countryCode", required = true) String countryCode, @RequestParam(name = "date", required = false) String date) throws JsonMappingException, JsonProcessingException {
        return new ResponseEntity<HolidayResponse>(holidayService.getNearestHoliday(countryCode, date), HttpStatus.OK);
    }

}
