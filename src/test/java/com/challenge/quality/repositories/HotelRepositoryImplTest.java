package com.challenge.quality.repositories;

import com.challenge.quality.dtos.HotelDTO;
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

class HotelRepositoryImplTest {
    private HotelRepository hotelRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws IOException {
        hotelRepository = new HotelRepositoryImpl("src/test/resources/hotels/allHotelTest.csv");
    }

    @Test
    @DisplayName("Should list all the hotels")
    void loadDataBase() throws IOException {
        // The module teaches the ObjectMapper how to work with LocalDates
        // and the parameter WRITE_DATES_AS_TIMESTAMPS tells the mapper to
        // represent a Date as a String in JSON.
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // It brings up the list of hotels from the allHotelTest.json file
        List<HotelDTO> hotels = objectMapper.readValue(
                new File("src/test/resources/hotels/allHotelTest.json"),
                new TypeReference<>() {
                });

        Assertions.assertIterableEquals(hotelRepository.getHotels(), hotels);
    }

    @Test
    @DisplayName("Should update the hotel reserved value")
    void updateReserved(){
        this.hotelRepository.updateReserved("CH-0003");

        Assertions.assertEquals(2, hotelRepository.getHotelsByReserved().size());
    }

    @Test
    @DisplayName("Should get the hotel CH-0002")
    void getHotelByFilterCode() throws DataNotFound, IOException {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        List<HotelDTO> filtered = objectMapper.readValue(
                new File("src/test/resources/hotels/filteredHotelCode.json"),
                new TypeReference<>() {
                });

        Assertions.assertEquals(filtered, this.hotelRepository.getHotelByFilter("10/02/2021", "12/02/2021", "Puerto Iguazú", "CH-0002"));
    }

    @Test
    @DisplayName("Should get the hotels CH-0002 and CH-0003")
    void getHotelByFilter() throws DataNotFound, IOException {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        List<HotelDTO> filtered = objectMapper.readValue(
                new File("src/test/resources/hotels/filteredHotel.json"),
                new TypeReference<>() {
                });

        Assertions.assertEquals(filtered, this.hotelRepository.getHotelByFilter("10/02/2021", "12/02/2021", "Puerto Iguazú", null));
    }

    @Test
    @DisplayName("Should list all the available hotels")
    void getHotelsByReserved() throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        List<HotelDTO> allHotel = objectMapper.readValue(
                new File("src/test/resources/hotels/allHotelTest.json"),
                new TypeReference<>() {
                });
        Assertions.assertIterableEquals(allHotel, this.hotelRepository.getHotelsByReserved());
    }

    @Test
    @DisplayName("Should throw an IOException")
    void ioException() throws IOException {
        Assertions.assertThrows(IOException.class, () -> new HotelRepositoryImpl("wrongPath"));
    }
}