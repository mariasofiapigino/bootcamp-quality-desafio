package com.challenge.quality.services;

import com.challenge.quality.dtos.BookingResponseDTO;
import com.challenge.quality.dtos.FlightDTO;
import com.challenge.quality.dtos.FlightRequestDTO;
import com.challenge.quality.dtos.FlightResponseDTO;
import com.challenge.quality.exceptionHandler.DataNotFound;
import com.challenge.quality.exceptionHandler.InvalidData;
import com.challenge.quality.repositories.FlightRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.openMocks;

class FlightServiceImplTest {
    private FlightService flightService;
    @Mock
    private FlightRepository flightRepository;
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<FlightDTO> allFlights;
    private List<FlightDTO> filteredFlight;
    private FlightRequestDTO flightRequest;
    private FlightRequestDTO flightInvalidRequest;

    @BeforeEach
    private void setUp() throws IOException {
        openMocks(this);
        this.flightService = new FlightServiceImpl(flightRepository);

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // It brings up the list of flights from the allFlights.json file
        allFlights = objectMapper.readValue(
                new File("src/test/resources/flights/allFlights.json"),
                new TypeReference<>() {
                });
        // It brings up the list of flights from the filterFlight.json file
        filteredFlight = objectMapper.readValue(
                new File("src/test/resources/flights/filterFlight.json"),
                new TypeReference<>() {
                });
        flightRequest = objectMapper.readValue(
                new File("src/test/resources/flights/flightRequest.json"),
                new TypeReference<>() {
                });
    }

    @Test
    @DisplayName("Should list all the flights")
    void getFlights() throws InvalidData, DataNotFound {
        Mockito.when(this.flightRepository.getFlights()).thenReturn(allFlights);

        assertIterableEquals(allFlights, this.flightService.getFlights(null, null, null, null));
    }

    @Test
    @DisplayName("Should list flights filtered")
    void getFlightsFiltered() throws DataNotFound, InvalidData {
        Mockito.when(this.flightRepository.getFlights()).thenReturn(allFlights);
        Mockito.when(this.flightRepository.getFlightsByFilter(
                "10/02/2021", "15/02/2021", "Buenos Aires", "Puerto Iguazú"))
        .thenReturn(filteredFlight);

        assertIterableEquals(filteredFlight, this.flightService.getFlights("10/02/2021", "15/02/2021", "Buenos Aires", "Puerto Iguazú"));
    }

    @Test
    @DisplayName("Should throw InvalidData exception (missing a filter)")
    void invalidData() throws IOException {
        Mockito.when(this.flightRepository.getFlights()).thenReturn(allFlights);

        assertThrows(InvalidData.class, () ->this.flightService.getFlights(
                "12/02/2021", null, "Buenos Aires", "Puerto Iguazú"));
    }

    @Test
    @DisplayName("Should throw DataNotFound exception (place doesn't exist)")
    void dataNotFoundPlace() throws IOException {
        Mockito.when(this.flightRepository.getFlights()).thenReturn(allFlights);

        assertThrows(DataNotFound.class, () -> this.flightService.getFlights(
                "12/02/2021", "19/03/2021", "Cordoba", "Buenos Aires"));
    }

    @Test
    @DisplayName("Should answer with a FlightResponse")
    void flightReservation() throws DataNotFound, IOException, InvalidData {
        Mockito.when(this.flightRepository.getFlights()).thenReturn(allFlights);
        Mockito.when(this.flightRepository.getFlightsByFilter("10/02/2021", "15/02/2021", "Buenos Aires", "Puerto Iguazú")).thenReturn(filteredFlight);

        FlightResponseDTO expected = objectMapper.readValue(
                new File("src/test/resources/flights/flightResponseService.json"),
                new TypeReference<>() {
                });

        assertEquals(expected, flightService.flightReservation(flightRequest));
    }

    @Test
    @DisplayName("Should throw InvalidData exception")
    void invalidDataUsername() throws IOException {
        flightInvalidRequest = objectMapper.readValue(
                new File("src/test/resources/flights/invalidFlightRequest.json"),
                new TypeReference<>() {
                });

        assertThrows(InvalidData.class, () ->this.flightService.flightReservation(
                flightInvalidRequest));
    }
}