package com.challenge.quality.repositories;

import com.challenge.quality.dtos.FlightDTO;
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
public class FlightRepositoryImpl implements FlightRepository{
    private List<FlightDTO> repository;
    private String path;
    private static String PATTERN = "dd/MM/yyyy";

    // The database is loaded as soon as the class initializes
    // Se define por configuracion y si no hay un path por defecto
    public FlightRepositoryImpl(@Value("${flights_path:src/main/resources/dbFlights.csv}") String path) throws IOException {
        this.path = path;
        this.repository = loadDataBase();
    }

    @Override
    public List<FlightDTO> getFlights(){
        return repository;
    }

    @Override
    public List<FlightDTO> getFlightsByFilter(String dateFrom, String dateTo, String origin, String destination) throws DataNotFound {
        List<FlightDTO> result;
        Predicate<FlightDTO> filterDateFrom = flight -> flight.getDateFrom().isBefore(LocalDate.parse(dateFrom, DateTimeFormatter.ofPattern(PATTERN)).plusDays(1));
        Predicate<FlightDTO> filterDateTo = flight -> flight.getDateTo().isAfter(LocalDate.parse(dateTo, DateTimeFormatter.ofPattern(PATTERN)).minusDays(1));
        Predicate<FlightDTO> filterOrigin = flight -> flight.getOrigin().toLowerCase(Locale.ROOT).equals(origin.toLowerCase(Locale.ROOT));
        Predicate<FlightDTO> filterDestination = flight -> flight.getDestination().toLowerCase(Locale.ROOT).equals(destination.toLowerCase(Locale.ROOT));
        result = repository.stream().filter(filterDateFrom.and(filterDateTo).and(filterOrigin).and(filterDestination)).collect(Collectors.toList());
        if (result.isEmpty()) throw new DataNotFound("There are no flights for those dates and those places");
        return result;
    }

    // It returns a list of all hotels
    public List<FlightDTO> loadDataBase() throws IOException {
        ArrayList<FlightDTO> flights = new ArrayList<>();
        BufferedReader bufferReader = null;
        try {
            File file = ResourceUtils.getFile(path);
            bufferReader = new BufferedReader(new FileReader(file));
            bufferReader.readLine();
            String line = bufferReader.readLine();

            while (line != null) {
                String[] attributes = line.split(",");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                FlightDTO flightDTO = new FlightDTO(
                        attributes[0].trim(), // number
                        attributes[1].trim(), // origin
                        attributes[2].trim(), // destination
                        attributes[3].trim(), // seatType
                        Integer.parseInt(attributes[4].trim().replace("$","").replace(".","")), // pricePerson
                        LocalDate.parse(attributes[5].trim(), formatter), // dateFrom
                        LocalDate.parse(attributes[6].trim(), formatter)); // dateTo

                flights.add(flightDTO);
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
        return flights;
    }
}
