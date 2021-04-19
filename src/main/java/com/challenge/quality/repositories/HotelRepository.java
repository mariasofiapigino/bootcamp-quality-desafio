package com.challenge.quality.repositories;

import com.challenge.quality.dtos.HotelDTO;
import com.challenge.quality.exceptionHandler.DataNotFound;

import java.io.IOException;
import java.util.List;

public interface HotelRepository {
    List<HotelDTO> getHotels() throws IOException;

    void updateReserved(String hotelCode);

    List<HotelDTO> getHotelByFilter(String dateFrom, String dateTo, String place, String hotelCode) throws DataNotFound;

    List<HotelDTO> getHotelsByReserved();
}
