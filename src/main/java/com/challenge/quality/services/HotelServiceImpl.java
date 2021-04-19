package com.challenge.quality.services;

import com.challenge.quality.dtos.*;
import com.challenge.quality.exceptionHandler.DataNotFound;
import com.challenge.quality.exceptionHandler.InvalidData;
import com.challenge.quality.repositories.HotelRepository;
import com.challenge.quality.utils.Interests;
import com.challenge.quality.utils.Validators;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements HotelService{
    private final HotelRepository hotelRepository;
    private static final String PATTERN = "dd/MM/yyyy";

    public HotelServiceImpl (HotelRepository hotelRepository){
        this.hotelRepository = hotelRepository;
    }

    @Override
    public List<HotelDTO> getHotels(String dateFrom, String dateTo, String place, String hotelCode) throws DataNotFound, InvalidData, IOException {
        List<HotelDTO> hotels = hotelRepository.getHotels();
        List<HotelDTO> result = new ArrayList<>();

        if (dateFrom == null && dateTo == null && place == null){
            return hotelRepository.getHotelsByReserved();
        }

        if (dateFrom == null || dateTo == null || place == null) throw new InvalidData("Some filters are missing");

        if (Validators.validDate(dateFrom) && Validators.validDate(dateTo)
                && Validators.validDate(dateFrom, dateTo) && existingPlace(hotels, place))
            result = hotelRepository.getHotelByFilter(dateFrom, dateTo, place, hotelCode);

        return result;
    }

    @Override
    public BookingResponseDTO bookHotel(BookingRequestDTO bookingRequestDTO) throws InvalidData, DataNotFound, IOException {
        if (bookingRequestDTO.getUsername() == null || bookingRequestDTO.getBooking() == null) throw new InvalidData("Invalid Booking Request");

        Validators.validEmail(bookingRequestDTO.getUsername());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
        String dateFrom = bookingRequestDTO.getBooking().getDateFrom().format(formatter);
        String dateTo = bookingRequestDTO.getBooking().getDateTo().format(formatter);

        List<HotelDTO> hotel = getHotels(dateFrom, dateTo, bookingRequestDTO.getBooking().getDestination(), bookingRequestDTO.getBooking().getHotelCode());

        Validators.validPeople(
                bookingRequestDTO.getBooking().getPeople().length,
                bookingRequestDTO.getBooking().getPeopleAmount(),
                bookingRequestDTO.getBooking().getRoomType());

        long days = bookingRequestDTO.getBooking().getDateFrom().until(bookingRequestDTO.getBooking().getDateTo(), ChronoUnit.DAYS);
        long amount = calculateAmount(hotel, days);
        Double interest = Interests.calculateInterest(bookingRequestDTO.getBooking().getPaymentMethod(), amount);

        hotelRepository.updateReserved(hotel.get(0).getHotelCode());

        return new BookingResponseDTO(
                bookingRequestDTO.getUsername(), (double) amount,interest, amount + interest,
                bookingRequestDTO.getBooking(), new StatusCodeDTO(200, "The process ended satisfactorily"));
    }

    private long calculateAmount(List<HotelDTO> hotel, long days) {
        return hotel.get(0).getNightPrice() * days;
    }

    private boolean existingPlace(List<HotelDTO> hotels, String place) throws DataNotFound {
        List<HotelDTO> hotelsPlace = hotels.stream().filter(
                hotelDTO -> hotelDTO.getPlace().toLowerCase(Locale.ROOT).equals(place.toLowerCase(Locale.ROOT)))
                .collect(Collectors.toList());
        if (!hotelsPlace.isEmpty())return true;
        else throw new DataNotFound("Non-existent destination");

    }
}
