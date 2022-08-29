package com.example.demo.controller.model;

import java.time.LocalDate;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class HolidayResponse {

	private LocalDate nearestHolidayDate;
	private String holidayName;
}
