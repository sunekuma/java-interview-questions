package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.persistence.HolidayEntity;

@Repository
public interface HolidayRespository extends JpaRepository<HolidayEntity, Integer> {

}
