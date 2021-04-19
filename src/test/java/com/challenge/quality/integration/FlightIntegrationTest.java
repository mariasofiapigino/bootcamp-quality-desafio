package com.challenge.quality.integration;

import com.challenge.quality.repositories.FlightRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FlightIntegrationTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    FlightRepository flightRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should list all flights")
    public void testListAllFlights() throws Exception {
        Path path = Paths.get("src/test/resources/flights/allFlights.json");

        String allFlightsMockResponse = Files.readString(path);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api2/v1/flights"))
                .andExpect(status().isOk())
                .andExpect(content().json(allFlightsMockResponse));
    }

    @Test
    @DisplayName("Should list BAPI-1235 flight")
    public void testFlightFilters() throws Exception {
        Path path = Paths.get("src/test/resources/flights/filterFlight.json");

        String filterFlight = Files.readString(path);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api2/v1/flights?origin=Buenos Aires&destination=Puerto Iguazú&dateFrom=10/02/2021&dateTo=15/02/2021"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(filterFlight));
    }

    @Test
    @DisplayName("Should response with a Flight Reservation")
    public void testFlightReservation() throws Exception {
        Path pathRequest = Paths.get("src/test/resources/flights/flightRequest.json");
        String flightRequest = Files.readString(pathRequest);

        Path pathResponse = Paths.get("src/test/resources/flights/flightResponse.json");
        String flightResponse = Files.readString(pathResponse);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api2/v1/flight-reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(flightRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(flightResponse));
    }

    @Test
    @DisplayName("Should throw a DataNotFound exception")
    public void testDataNotFound() throws Exception {
        Path path = Paths.get("src/test/resources/flights/dataNotFound.json");

        String flightNotFound = Files.readString(path);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api2/v1/flights?origin=Buenos Aires&destination=Puerto Iguazú&dateFrom=10/02/2020&dateTo=15/02/2020"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json(flightNotFound));
    }

    @Test
    @DisplayName("Should throw an InvalidData exception")
    public void testInvalidData() throws Exception {
        Path path = Paths.get("src/test/resources/hotels/invalidData.json");

        String invalidData = Files.readString(path);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api2/v1/flights?origin=Buenos Aires&dateTo=15/02/2020"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(invalidData));
    }

    @Test
    @DisplayName("Should throw an InvalidFormatException exception")
    public void testInvalidFormatException() throws Exception {
        Path path = Paths.get("src/test/resources/flights/invalidFormatException.json");

        String invalidFormat = Files.readString(path);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api2/v1/flight-reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidFormat))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
