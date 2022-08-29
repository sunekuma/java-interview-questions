package com.example.demo.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@NoArgsConstructor
@SuperBuilder
@Table(name="Holiday_Detail")
public class HolidayEntity {
 
    @Id
    @Column(name="holiday_year", nullable=false)
    private Integer year;
    
    @Column(name="holiday_data", nullable=false)
    @Lob
    private String data;
}
