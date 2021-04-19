package com.challenge.quality.repositories;

import com.challenge.quality.dtos.HotelDTO;
import com.challenge.quality.exceptionHandler.DataNotFound;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class HotelRepositoryImpl implements HotelRepository{
    private List<HotelDTO> repository;
    private String path;
    private static final String PATTERN = "dd/MM/yyyy";

    // The database is loaded as soon as the class initializes
    // Se define por configuracion y si no hay un path por defecto
    public HotelRepositoryImpl(@Value("${hotels_path:src/main/resources/dbHotels.csv}") String path) throws IOException {
        this.path = path;
        this.repository = loadDataBase();
    }

    @Override
    public List<HotelDTO> getHotels(){
        return repository;
    }

    // It returns a list of all hotels
    public List<HotelDTO> loadDataBase() throws IOException {
        ArrayList<HotelDTO> hotels = new ArrayList<>();
        BufferedReader bufferReader = null;
        try {
            File file = ResourceUtils.getFile(path);
            bufferReader = new BufferedReader(new FileReader(file));
            bufferReader.readLine();
            String line = bufferReader.readLine();

            while (line != null) {
                String[] attributes = line.split(",");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                HotelDTO hotelDTO = new HotelDTO(
                        attributes[0].trim(), // hotelCode
                        attributes[1].trim(), // name
                        attributes[2].trim(), // place
                        attributes[3].trim(), // typeRoom
                        Integer.parseInt(attributes[4].trim().replace("$","")), // nightPrice
                        LocalDate.parse(attributes[5].trim(), formatter), // availableFrom
                        LocalDate.parse(attributes[6].trim(), formatter), // availableUntil
                        attributes[7].trim().equals("SI")); // reserved

                hotels.add(hotelDTO);
                line = bufferReader.readLine();
            }
        } catch (IOException e) {
            throw new IOException("Error opening file");
        } finally {
            if (bufferReader != null) {
                try {
                    bufferReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return hotels;
    }

    @Override
    public void updateReserved(String hotelCode) {
        for (int i = 0; i < repository.size(); i++) {
            if (repository.get(i).getHotelCode().equals(hotelCode)){
                repository.get(i).setReserved(true);
            }
        }
    }

    @Override
    public List<HotelDTO> getHotelByFilter(String dateFrom, String dateTo, String place, String hotelCode) throws DataNotFound {
        List<HotelDTO> result;
        Predicate<HotelDTO> filterDateFrom = hotel -> hotel.getAvailableFrom().isBefore(LocalDate.parse(dateFrom, DateTimeFormatter.ofPattern(PATTERN)).plusDays(1));
        Predicate<HotelDTO> filterDateTo = hotel -> hotel.getAvailableUntil().isAfter(LocalDate.parse(dateTo, DateTimeFormatter.ofPattern(PATTERN)).minusDays(1));
        Predicate<HotelDTO> filterPlace = hotel -> hotel.getPlace().toLowerCase(Locale.ROOT).equals(place.toLowerCase(Locale.ROOT));
        Predicate<HotelDTO> filterReserved = hotel -> !hotel.getReserved();
        if (hotelCode == null) result = repository.stream().filter(filterDateFrom.and(filterDateTo).and(filterPlace).and(filterReserved)).collect(Collectors.toList());
        else {
            Predicate<HotelDTO> filterCode = hotel -> hotel.getHotelCode().toLowerCase(Locale.ROOT).equals(hotelCode.toLowerCase(Locale.ROOT));
            result = repository.stream().filter(filterDateFrom.and(filterDateTo).and(filterPlace).and(filterReserved).and(filterCode)).collect(Collectors.toList());
        }
        if (result.isEmpty()) throw new DataNotFound("There are no hotels available for those dates in that place");
        return result;
    }

    @Override
    public List<HotelDTO> getHotelsByReserved() {
        return repository.stream().filter(hotel -> !hotel.getReserved()).collect(Collectors.toList());
    }
}
