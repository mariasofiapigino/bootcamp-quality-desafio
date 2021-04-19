package com.challenge.quality.services;

import com.challenge.quality.dtos.BookingRequestDTO;
import com.challenge.quality.dtos.BookingResponseDTO;
import com.challenge.quality.dtos.HotelDTO;
import com.challenge.quality.exceptionHandler.DataNotFound;
import com.challenge.quality.exceptionHandler.InvalidData;

import java.io.IOException;
import java.util.List;

public interface HotelService {
    List<HotelDTO> getHotels(String dateFrom, String dateTo, String place, String hotelCode) throws DataNotFound, InvalidData, IOException;

    BookingResponseDTO bookHotel(BookingRequestDTO bookingRequestDTO) throws InvalidData, DataNotFound, IOException;
}
