package com.challenge.quality.controllers;

import com.challenge.quality.dtos.FlightRequestDTO;
import com.challenge.quality.dtos.StatusCodeDTO;
import com.challenge.quality.exceptionHandler.DataNotFound;
import com.challenge.quality.exceptionHandler.InvalidData;
import com.challenge.quality.services.FlightService;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api2/v1")
public class FlightController {
    private final FlightService flightService;

    public FlightController (FlightService flightService){
        this.flightService = flightService;
    }

    @GetMapping("/flights")
    public ResponseEntity getProducts(@RequestParam(required = false) String dateFrom,
                                      @RequestParam(required = false) String dateTo,
                                      @RequestParam(required = false) String origin,
                                      @RequestParam(required = false) String destination
                                      ) throws InvalidData, DataNotFound {
        return ResponseEntity.ok(flightService.getFlights(dateFrom, dateTo, origin, destination));
    }

    @PostMapping("/flight-reservation")
    public ResponseEntity flightReservation(@RequestBody FlightRequestDTO flightRequestDTO) throws InvalidData, DataNotFound, IOException {
        return ResponseEntity.ok(flightService.flightReservation(flightRequestDTO));
    }

    // Errors handler
    // TODO un Exception Handler general
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
