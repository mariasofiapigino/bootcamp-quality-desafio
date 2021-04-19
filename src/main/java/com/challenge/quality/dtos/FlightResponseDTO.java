package com.challenge.quality.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightResponseDTO {
    private String username;
    private Double amount;
    private Double interest;
    private Double total;
    private FlightReservationDTO flightReservation;
    private StatusCodeDTO statusCode;
}
