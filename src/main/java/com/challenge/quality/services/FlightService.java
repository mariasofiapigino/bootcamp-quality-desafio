package com.challenge.quality.services;

import com.challenge.quality.dtos.FlightDTO;
import com.challenge.quality.dtos.FlightRequestDTO;
import com.challenge.quality.dtos.FlightResponseDTO;
import com.challenge.quality.exceptionHandler.DataNotFound;
import com.challenge.quality.exceptionHandler.InvalidData;

import java.util.List;

public interface FlightService {
    List<FlightDTO> getFlights(String dateFrom, String dateTo, String origin, String destination) throws InvalidData, DataNotFound;

    FlightResponseDTO flightReservation(FlightRequestDTO flightRequestDTO) throws InvalidData, DataNotFound;
}
