package com.challenge.quality.services;

import com.challenge.quality.dtos.*;
import com.challenge.quality.exceptionHandler.DataNotFound;
import com.challenge.quality.exceptionHandler.InvalidData;
import com.challenge.quality.repositories.FlightRepository;
import com.challenge.quality.utils.Interests;
import com.challenge.quality.utils.Validators;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl implements FlightService{
    private final FlightRepository flightRepository;
    private static String PATTERN = "dd/MM/yyyy";

    public FlightServiceImpl (FlightRepository flightRepository){
        this.flightRepository = flightRepository;
    }

    @Override
    public List<FlightDTO> getFlights(String dateFrom, String dateTo, String origin, String destination) throws InvalidData, DataNotFound {
        List<FlightDTO> flights = flightRepository.getFlights();
        List<FlightDTO> result = new ArrayList<>();

        if (dateFrom == null && dateTo == null && origin == null && destination == null)
            return flights;

        if (dateFrom == null || dateTo == null || origin == null || destination == null) throw new InvalidData("Some filters are missing");

        Predicate<FlightDTO> filterOrigin = flightDTO -> flightDTO.getOrigin().toLowerCase(Locale.ROOT).equals(origin.toLowerCase(Locale.ROOT));
        Predicate<FlightDTO> filterDestination = flightDTO -> flightDTO.getDestination().toLowerCase(Locale.ROOT).equals(destination.toLowerCase(Locale.ROOT));

        if (Validators.validDate(dateFrom) && Validators.validDate(dateTo)
                && Validators.validDate(dateFrom, dateTo) && existingPlace(flights, filterOrigin) && existingPlace(flights, filterDestination))
            result = flightRepository.getFlightsByFilter(dateFrom, dateTo, origin, destination);
        return result;
    }

    @Override
    public FlightResponseDTO flightReservation(FlightRequestDTO flightRequestDTO) throws InvalidData, DataNotFound {
        if (flightRequestDTO.getUsername() == null || flightRequestDTO.getFlightReservation() == null) throw new InvalidData("Invalid Flight Request");

        Validators.validEmail(flightRequestDTO.getUsername());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
        String dateFrom = flightRequestDTO.getFlightReservation().getDateFrom().format(formatter);
        String dateTo = flightRequestDTO.getFlightReservation().getDateTo().format(formatter);

        List<FlightDTO> flight = getFlights(dateFrom, dateTo, flightRequestDTO.getFlightReservation().getOrigin(), flightRequestDTO.getFlightReservation().getDestination());

        Validators.validPeople(
                flightRequestDTO.getFlightReservation().getPeople().length,
                flightRequestDTO.getFlightReservation().getSeats(), null);

        long amount = flight.get(0).getPricePerson() * flightRequestDTO.getFlightReservation().getSeats();
        Double interest = Interests.calculateInterest(flightRequestDTO.getFlightReservation().getPaymentMethod(), amount);

        return new FlightResponseDTO(
                flightRequestDTO.getUsername(), (double) amount, interest, amount + interest,
                flightRequestDTO.getFlightReservation(), new StatusCodeDTO(200, "The process ended satisfactorily"));
    }

    private boolean existingPlace(List<FlightDTO> flights, Predicate filter) throws DataNotFound {
        List<FlightDTO> flightsPlace = (List<FlightDTO>) flights.stream().filter(filter)
                .collect(Collectors.toList());
        if (!flightsPlace.isEmpty()){
            return true;
        } else {
            throw new DataNotFound("Non-existent place");
        }
    }
}
