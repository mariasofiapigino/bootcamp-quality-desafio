package com.challenge.quality.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class FlightReservationDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "es_AR",
            timezone = "America/Argentina/Buenos_Aires")
    private LocalDate dateFrom;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "es_AR",
            timezone = "America/Argentina/Buenos_Aires")
    private LocalDate dateTo;
    private String origin;
    private String destination;
    private String flightNumber;
    private Integer seats;
    private String seatType;
    private PeopleDTO[] people;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private PaymentMethodDTO paymentMethod;
}
