package com.challenge.quality.repositories;

import com.challenge.quality.dtos.FlightDTO;
import com.challenge.quality.exceptionHandler.DataNotFound;

import java.util.List;

public interface FlightRepository {
    List<FlightDTO> getFlights();

    List<FlightDTO> getFlightsByFilter(String dateFrom, String dateTo, String origin, String destination) throws DataNotFound;
}
