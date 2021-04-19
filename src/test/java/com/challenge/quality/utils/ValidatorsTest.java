package com.challenge.quality.utils;

import com.challenge.quality.exceptionHandler.InvalidData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorsTest {

    @Test
    @DisplayName("Should try a valid date")
    void validDate() throws InvalidData {
        assertTrue(Validators.validDate("31/12/2021"));
    }

    @Test
    @DisplayName("Should try a valid date range")
    void testValidDate() throws InvalidData {
        assertTrue(Validators.validDate("26/12/2021", "31/12/2021"));
    }

    @Test
    @DisplayName("Should try an invalid date")
    void invalidDate() {
        assertThrows(InvalidData.class,
                () -> Validators.validDate("40/12/2021"),
                "Invalid Date");
    }

    @Test
    @DisplayName("Should try an invalid date range")
    void testInvalidDate() {
        assertThrows(InvalidData.class,
                () -> Validators.validDate("12/12/2021","10/12/2021"),
                "Invalid Date");
    }

    @Test
    @DisplayName("Should try a valid email")
    void validEmail() throws InvalidData {
        assertTrue(Validators.validEmail("seba_gonzalez@unmail.com.ar"));
    }

    @Test
    @DisplayName("Should try an invalid email")
    void testInvalidEmail() {
        assertThrows(InvalidData.class,
                () -> Validators.validEmail("seba_gonzalezunmail.com.ar"),
                "Invalid Date");
    }

    @Test
    @DisplayName("Should try 1 people in a double room")
    void testInvalidPeopleOneInDouble(){
        assertThrows(InvalidData.class, () -> Validators.validPeople(1, 1, "DOBLE"));
    }

    @Test
    @DisplayName("Should try 2 people in a single room")
    void testInvalidPeopleTwoInSingle(){
        assertThrows(InvalidData.class, () -> Validators.validPeople(2, 2, "SINGLE"));
    }

    @Test
    @DisplayName("Should try 3 people in a single room")
    void testInvalidPeopleThreeInSingle(){
        assertThrows(InvalidData.class, () -> Validators.validPeople(3, 3, "SINGLE"));
    }

    @Test
    @DisplayName("Should try 5 people in a single room")
    void testInvalidPeopleFiveInSingle(){
        assertThrows(InvalidData.class, () -> Validators.validPeople(5, 5, "SINGLE"));
    }

    @Test
    @DisplayName("Should try 2 people registering only 1")
    void testInvalidPeople(){
        assertThrows(InvalidData.class, () -> Validators.validPeople(2, 1, "SINGLE"));
    }
}