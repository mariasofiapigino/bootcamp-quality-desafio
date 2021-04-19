package com.challenge.quality.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookingRequestDTO {
    private String username;
    private BookingDTO booking;
}
