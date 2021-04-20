package com.challenge.quality.repositories;

import com.challenge.quality.dtos.FlightDTO;
import com.challenge.quality.exceptionHandler.DataNotFound;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

class FlightRepositoryImplTest {
    private FlightRepository flightRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws IOException {
        flightRepository = new FlightRepositoryImpl("src/test/resources/flights/allFlights.csv");
    }

    @Test
    @DisplayName("Should list all the flights")
    void loadDataBase() throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // It brings up the list of flights from the allFlights.json file
        List<FlightDTO> flights = objectMapper.readValue(
                new File("src/test/resources/flights/allFlights.json"),
                new TypeReference<>() {
                });

        Assertions.assertIterableEquals(flightRepository.getFlights(), flights);
    }

    @Test
    @DisplayName("Should get flight BAPI-1235")
    void getFlightsByFilter() throws IOException, DataNotFound {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        List<FlightDTO> filtered = objectMapper.readValue(
                new File("src/test/resources/flights/filterFlight.json"),
                new TypeReference<>() {
                });

        Assertions.assertEquals(filtered, this.flightRepository.getFlightsByFilter("10/02/2021", "15/02/2021", "Buenos Aires", "Puerto Iguazú"));
    }

    @Test
    @DisplayName("Should throw an IOException")
    void ioException() {
        Assertions.assertThrows(IOException.class, () -> new FlightRepositoryImpl("wrongPath"));
    }

}