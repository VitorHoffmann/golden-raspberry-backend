package com.example.goldenraspberrybackend.util;

import com.example.goldenraspberrybackend.model.Movie;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvUtils {

    public static List<Movie> readCsv(String fileName) {
        List<Movie> movies = new ArrayList<>();
        try (InputStreamReader fileReader = new InputStreamReader(new ClassPathResource(fileName).getInputStream())) {

            CSVParser parser = new CSVParserBuilder()
                    .withSeparator(';')
                    .build();

            CSVReader reader = new CSVReaderBuilder(fileReader)
                    .withCSVParser(parser)
                    .build();

            String[] line;
            boolean headerSkipped = false;

            while ((line = reader.readNext()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }
                Movie movie = new Movie();
                movie.setYear(Integer.parseInt(line[0]));
                movie.setTitle(line[1]);
                movie.setStudios(line[2]);
                movie.setProducers(line[3]);
                movie.setWinner(line.length > 4 && line[4].equals("yes"));
                movies.add(movie);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading CSV file: " + "data.csv", e);
        }
        return movies;
    }
}
