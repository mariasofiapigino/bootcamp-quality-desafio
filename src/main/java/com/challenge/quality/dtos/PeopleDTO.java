package com.challenge.quality.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PeopleDTO {
    private String dni;
    private String name;
    private String lastname;
    private String birthDate;
    private String mail;
}
