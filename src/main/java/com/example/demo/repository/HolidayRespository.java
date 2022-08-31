package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.persistence.HolidayEntity;

@Repository
public interface HolidayRespository extends JpaRepository<HolidayEntity, Long> {
	
	Optional<HolidayEntity> findHolidaysByCountryAndYear(String coutryCode, Integer year);
}
