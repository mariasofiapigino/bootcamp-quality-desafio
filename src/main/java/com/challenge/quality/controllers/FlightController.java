package com.challenge.quality.controllers;

import com.challenge.quality.dtos.FlightRequestDTO;
import com.challenge.quality.exceptionHandler.DataNotFound;
import com.challenge.quality.exceptionHandler.InvalidData;
import com.challenge.quality.services.FlightService;
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
}
