package com.challenge.quality.utils;

import com.challenge.quality.exceptionHandler.InvalidData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Pattern;

public class Validators {
    public static boolean validDate(String dateStr) throws InvalidData {
        try {
            LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate.of(
                    Integer.parseInt(dateStr.substring(6,10)),
                    Integer.parseInt(dateStr.substring(3,5)),
                    Integer.parseInt(dateStr.substring(0,2))
                    );
        } catch (Exception e) {
            throw new InvalidData("Date must be valid and format must be dd/mm/yyyy");
        }
        return true;
    }

    public static boolean validDate(String dateFrom, String dateTo) throws InvalidData {
        if (LocalDate.parse(dateFrom, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                .isBefore(LocalDate.parse(dateTo, DateTimeFormatter.ofPattern("dd/MM/yyyy")))) return true;
        else throw new InvalidData("Date from must be before date to");
    }

    public static void validPeople(int peopleArray, int peopleAmount, String roomType) throws InvalidData {
        if (peopleArray == peopleAmount){
            if (roomType == null) return;
            switch (peopleArray){
                case 1:
                    if (!roomType.toLowerCase(Locale.ROOT).trim().equals("single"))
                        throw new InvalidData("The number of people does not correspond to the size of the rooms");
                    break;
                case 2:
                    if (!roomType.toLowerCase(Locale.ROOT).trim().equals("doble"))
                        throw new InvalidData("The number of people does not correspond to the size of the rooms");
                    break;
                case 3:
                    if (!roomType.toLowerCase(Locale.ROOT).trim().equals("triple"))
                        throw new InvalidData("The number of people does not correspond to the size of the rooms");
                    break;
                default:
                    if (!roomType.toLowerCase(Locale.ROOT).trim().equals("múltiple"))
                        throw new InvalidData("The number of people does not correspond to the size of the rooms");
                    break;
            }
        } else {
            throw new InvalidData("The number of people does not correspond to the number of people registered");
        }
    }

    public static boolean validEmail(String username) throws InvalidData {
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        if (!pattern.matcher(username).find()) {
            throw new InvalidData("Invalid email format");
        }
        return true;
    }

    // TODO ver si se puede reutilizar
    /*private boolean existingPlace(List<Reservable> list, Predicate filter) throws DataNotFound {
        List<Reservable> listPlace = (List<Reservable>) list.stream().filter(filter)
                .collect(Collectors.toList());
        if (!listPlace.isEmpty()){
            return true;
        } else {
            throw new DataNotFound("Non-existent destination");
        }
    }*/
}
