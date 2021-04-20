package com.challenge.quality.exceptionHandler;

import com.challenge.quality.dtos.StatusCodeDTO;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestHandler {

    @ExceptionHandler(DataNotFound.class)
    public ResponseEntity<StatusCodeDTO> handleException(DataNotFound e) {
        StatusCodeDTO statusCodeDTO = new StatusCodeDTO(404, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(statusCodeDTO);
    }

    @ExceptionHandler(InvalidData.class)
    public ResponseEntity<StatusCodeDTO> handleException(InvalidData e) {
        StatusCodeDTO statusCodeDTO = new StatusCodeDTO(400, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCodeDTO);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<StatusCodeDTO> handleException(InvalidFormatException e) {
        StatusCodeDTO statusCodeDTO = new StatusCodeDTO(400, "Data entered with invalid format: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCodeDTO);
    }
}
