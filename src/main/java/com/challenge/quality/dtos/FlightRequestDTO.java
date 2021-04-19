package com.challenge.quality.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FlightRequestDTO {
    private String username;
    private FlightReservationDTO flightReservation;
}
