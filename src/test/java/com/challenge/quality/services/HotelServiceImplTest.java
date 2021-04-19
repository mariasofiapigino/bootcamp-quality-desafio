package com.challenge.quality.services;

import com.challenge.quality.dtos.*;
import com.challenge.quality.exceptionHandler.DataNotFound;
import com.challenge.quality.exceptionHandler.InvalidData;
import com.challenge.quality.repositories.HotelRepository;
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

class HotelServiceImplTest {

    private HotelService hotelService;
    @Mock
    private HotelRepository hotelRepository;
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<HotelDTO> allHotels;
    private List<HotelDTO> filteredHotels;
    private List<HotelDTO> bookingHotel;
    private BookingRequestDTO bookingInvalidRequestDTO;
    private BookingRequestDTO bookingRequest;

    @BeforeEach
    private void setUp() throws IOException {
        openMocks(this);
        this.hotelService = new HotelServiceImpl(hotelRepository);

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // It brings up the list of hotels from the dbHotels.json file
        allHotels = objectMapper.readValue(
                new File("src/test/resources/hotels/dbHotels.json"),
                new TypeReference<>() {
                });

        // It brings up the list of hotels from the filterHotel.json file
        filteredHotels = objectMapper.readValue(
                new File("src/test/resources/hotels/filterHotel.json"),
                new TypeReference<>() {
                });

        bookingRequest = objectMapper.readValue(
                new File("src/test/resources/hotels/bookingRequest.json"),
                new TypeReference<>() {
                });

        bookingHotel = objectMapper.readValue(
                new File("src/test/resources/hotels/bookingHotel.json"),
                new TypeReference<>() {
                });
    }

    @Test
    @DisplayName("Should list all the hotels")
    void getHotels() throws IOException, DataNotFound, InvalidData {
        Mockito.when(this.hotelRepository.getHotels()).thenReturn(allHotels);
        Mockito.when(this.hotelRepository.getHotelsByReserved()).thenReturn(allHotels);

        assertIterableEquals(allHotels, this.hotelService.getHotels(null, null, null, null));
    }

    @Test
    @DisplayName("Should list hotels filtered")
    void getHotelsFiltered() throws IOException, DataNotFound, InvalidData {
        Mockito.when(this.hotelRepository.getHotels()).thenReturn(allHotels);
        Mockito.when(this.hotelRepository.getHotelByFilter("10/02/2021", "12/02/2021",
                "Puerto Iguazú", null)).thenReturn(filteredHotels);
        // TODO Intente mockear el Utils pero no me deja

        assertIterableEquals(filteredHotels, this.hotelService.getHotels(
                "10/02/2021", "12/02/2021", "Puerto Iguazú", null));
    }

    @Test
    @DisplayName("Should throw InvalidData exception (missing a filter)")
    void invalidData() throws IOException {
        Mockito.when(this.hotelRepository.getHotels()).thenReturn(allHotels);

        assertThrows(InvalidData.class, () ->this.hotelService.getHotels(
                "12/02/2021", null, "Buenos Aires", null));
    }

    @Test
    @DisplayName("Should throw DataNotFound exception (place doesn't exist)")
    void dataNotFoundPlace() throws IOException {
        Mockito.when(this.hotelRepository.getHotels()).thenReturn(allHotels);

        assertThrows(DataNotFound.class, () -> this.hotelService.getHotels(
                "12/02/2021", "19/03/2021", "Cordoba", null));
    }

    @Test
    @DisplayName("Should answer with a BookingResponse")
    void bookHotel() throws IOException, InvalidData, DataNotFound {
        Mockito.when(this.hotelRepository.getHotels()).thenReturn(allHotels);
        Mockito.when(this.hotelRepository.getHotelByFilter("10/02/2021","12/02/2021",
                "Puerto Iguazú", "CH-0003")).thenReturn(bookingHotel);

        BookingResponseDTO expected = objectMapper.readValue(
                new File("src/test/resources/hotels/bookingResponseService.json"),
                new TypeReference<>() {
                });

        assertEquals(expected, hotelService.bookHotel(bookingRequest));
    }

    @Test
    @DisplayName("Should throw InvalidData exception")
    void invalidDataUsername() throws IOException {
        bookingInvalidRequestDTO = objectMapper.readValue(
                new File("src/test/resources/hotels/invalidBookingRequest.json"),
                new TypeReference<>() {
                });

        assertThrows(InvalidData.class, () ->this.hotelService.bookHotel(
                bookingInvalidRequestDTO));
    }
}