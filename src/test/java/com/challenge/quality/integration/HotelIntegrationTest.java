package com.challenge.quality.integration;

import com.challenge.quality.repositories.HotelRepository;
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
public class HotelIntegrationTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should list all available hotels")
    public void testListAllHotels() throws Exception {
        Path path = Paths.get("src/test/resources/hotels/dbHotels.json");

        String allHotelsMockResponse = Files.readString(path);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/hotels"))
                .andExpect(status().isOk())
                .andExpect(content().json(allHotelsMockResponse));
    }

    @Test
    @DisplayName("Should list all available hotels with filters")
    public void testListHotelsWithfilters() throws Exception {
        Path path = Paths.get("src/test/resources/hotels/filterHotel.json");

        String allHotelsWithFiltersMockResponse = Files.readString(path);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/hotels?dateFrom=12/02/2021&dateTo=19/03/2021&destination=Buenos Aires"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(allHotelsWithFiltersMockResponse));
    }

    @Test
    @DisplayName("Should response with a Booking Response")
    // TODO ver como solucioanr el cambio de reserved
    public void testBookHotel() throws Exception {
        Path pathRequest = Paths.get("src/test/resources/hotels/bookingRequest.json");
        String bookingRequest = Files.readString(pathRequest);

        Path pathResponse = Paths.get("src/test/resources/hotels/bookingResponse.json");
        String bookingResponse = Files.readString(pathResponse);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(bookingResponse));
    }

    @Test
    @DisplayName("Should throw a DataNotFound exception")
    public void testDataNotFound() throws Exception {
        Path path = Paths.get("src/test/resources/hotels/dataNotFound.json");

        String hotelNotFound = Files.readString(path);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/hotels?dateFrom=12/02/2020&dateTo=19/03/2020&destination=Buenos Aires"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json(hotelNotFound));
    }

    @Test
    @DisplayName("Should throw an InvalidData exception")
    public void testInvalidData() throws Exception {
        Path path = Paths.get("src/test/resources/hotels/invalidData.json");

        String invalidData = Files.readString(path);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/v1/hotels?dateFrom=10/02/2021&destination=Puerto Iguazú"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(invalidData));
    }

    @Test
    @DisplayName("Should throw an InvalidFormatException exception")
    public void testInvalidFormatException() throws Exception {
        Path path = Paths.get("src/test/resources/hotels/invalidFormatException.json");

        String invalidFormat = Files.readString(path);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidFormat))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
