package com.challenge.quality.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightDTO {
    private String number;
    private String origin;
    private String destination;
    private String seatType;
    private Integer pricePerson;
    private LocalDate dateFrom;
    private LocalDate dateTo;
}
