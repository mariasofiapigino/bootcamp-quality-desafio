package com.challenge.quality.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelDTO {
    private String hotelCode;
    private String name;
    private String place;
    private String typeRoom;
    private Integer nightPrice;
    private LocalDate availableFrom;
    private LocalDate availableUntil;
    private Boolean reserved;
}
