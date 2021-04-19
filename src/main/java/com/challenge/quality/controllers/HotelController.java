package com.challenge.quality.controllers;

import com.challenge.quality.dtos.BookingRequestDTO;
import com.challenge.quality.dtos.StatusCodeDTO;
import com.challenge.quality.exceptionHandler.DataNotFound;
import com.challenge.quality.exceptionHandler.InvalidData;
import com.challenge.quality.services.HotelService;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class HotelController {
    private final HotelService hotelService;

    public HotelController (HotelService hotelService){
        this.hotelService = hotelService;
    }

    @GetMapping("/hotels")
    public ResponseEntity getProducts(@RequestParam(required = false) String dateFrom,
                                      @RequestParam(required = false) String dateTo,
                                      @RequestParam(required = false) String destination ) throws InvalidData, IOException, DataNotFound {
        return ResponseEntity.ok(hotelService.getHotels(dateFrom, dateTo, destination, null));
    }

    @PostMapping("/booking")
    public ResponseEntity bookHotel(@RequestBody BookingRequestDTO bookingRequestDTO) throws InvalidData, DataNotFound, IOException {
        return ResponseEntity.ok(hotelService.bookHotel(bookingRequestDTO));
    }

    // Errors handler
    @ExceptionHandler(InvalidData.class)
    public ResponseEntity<StatusCodeDTO> handleException(InvalidData e) {
        StatusCodeDTO statusCodeDTO = new StatusCodeDTO(400, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCodeDTO);
    }

    @ExceptionHandler(DataNotFound.class)
    public ResponseEntity<StatusCodeDTO> handleException(DataNotFound e) {
        StatusCodeDTO statusCodeDTO = new StatusCodeDTO(404, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(statusCodeDTO);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<StatusCodeDTO> handleException(InvalidFormatException e) {
        StatusCodeDTO statusCodeDTO = new StatusCodeDTO(400, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCodeDTO);
    }
}
