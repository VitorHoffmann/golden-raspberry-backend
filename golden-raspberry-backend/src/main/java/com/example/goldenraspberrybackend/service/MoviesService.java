package com.example.goldenraspberrybackend.service;

import com.example.goldenraspberrybackend.model.AwardsInterval;
import com.example.goldenraspberrybackend.model.IntervalSummary;
import com.example.goldenraspberrybackend.model.Movie;
import com.example.goldenraspberrybackend.repository.MovieRepository;
import com.example.goldenraspberrybackend.util.CsvUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MoviesService {

    private final MovieRepository movieRepository;

    public MoviesService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @PostConstruct
    public void initDatabase() {
        if (movieRepository.count() == 0) {
            List<Movie> records = CsvUtils.readCsv("movielist.csv");
            movieRepository.saveAll(records);
            System.out.println("CSV importado e salvo no banco com sucesso!");
        } else {
            System.out.println("Banco já contém registros, importação ignorada.");
        }
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public IntervalSummary buildSummary () {
        IntervalSummary summary = new IntervalSummary();
        List<Movie> movies = getAllMovies();
        List<AwardsInterval> intervalList = new ArrayList<>();
        List<String> producersAux = movies.stream().map(Movie::getProducers).distinct().toList();
        List<String> producers = new ArrayList<>();
        producersAux.forEach(producer -> {
            String[] producerAux = producer.split(" and ");
            for (int i = 0; i < producerAux.length; i++) {
                String[] producerAux2 = producerAux[i].split(", ");
                for (int j = 0; j < producerAux2.length ; j++) {
                    if (!producers.contains(producerAux2[j])) {
                        producers.add(producerAux2[j]);
                    }
                }
            }
        });
        producers.sort(String::compareTo);
        for (String producer : producers) {
            List<Movie> moviesByProducer = movies.stream()
                    .filter(m -> m.getProducers().toUpperCase().contains(producer.toUpperCase()) && m.getWinner().equals(true))
                    .sorted(Comparator.comparingInt(Movie::getYear))
                    .toList();
            int shortestInterval = 300;
            int longestInterval = 0;
            boolean added = false;
            for (int i = 0; i < moviesByProducer.size() - 1; i++) {
                Movie movie1 = moviesByProducer.get(i);
                Movie movie2 = moviesByProducer.get(i + 1);
                int interval = movie2.getYear() - movie1.getYear();
                if (interval < shortestInterval) {
                    shortestInterval = interval;
                    AwardsInterval awardsIntervalShortest = new AwardsInterval();
                    awardsIntervalShortest.setProducer(producer);
                    awardsIntervalShortest.setInterval(shortestInterval);
                    awardsIntervalShortest.setPreviousWin(movie1.getYear());
                    awardsIntervalShortest.setFollowingWin(movie2.getYear());
                    intervalList.add(awardsIntervalShortest);
                    added = true;
                } else if (interval == shortestInterval) {
                    AwardsInterval awardsIntervalShortest = new AwardsInterval();
                    awardsIntervalShortest.setProducer(producer);
                    awardsIntervalShortest.setInterval(interval);
                    awardsIntervalShortest.setPreviousWin(movie1.getYear());
                    awardsIntervalShortest.setFollowingWin(movie2.getYear());
                    intervalList.add(awardsIntervalShortest);
                    added = true;
                }
                if (!added) {
                    if (interval > longestInterval) {
                        longestInterval = interval;
                        AwardsInterval awardsIntervalLongest = new AwardsInterval();
                        awardsIntervalLongest.setProducer(producer);
                        awardsIntervalLongest.setInterval(longestInterval);
                        awardsIntervalLongest.setPreviousWin(movie1.getYear());
                        awardsIntervalLongest.setFollowingWin(movie2.getYear());
                        intervalList.add(awardsIntervalLongest);
                    } else if (interval == longestInterval) {
                        AwardsInterval awardsIntervalLongest = new AwardsInterval();
                        awardsIntervalLongest.setProducer(producer);
                        awardsIntervalLongest.setInterval(interval);
                        awardsIntervalLongest.setPreviousWin(movie1.getYear());
                        awardsIntervalLongest.setFollowingWin(movie2.getYear());
                        intervalList.add(awardsIntervalLongest);
                    }
                }
            }
        }
        if (intervalList.isEmpty()) {
            summary.setMin(Collections.emptyList());
            summary.setMax(Collections.emptyList());
            return summary;
        }
        intervalList.sort(Comparator.comparing(AwardsInterval::getInterval));
        summary.setMin(intervalList.stream()
                .filter(i -> i.getInterval().equals(intervalList.get(0).getInterval()))
                .toList());
        summary.setMax(intervalList.stream()
                .filter(i -> i.getInterval().equals(intervalList.get(intervalList.size() - 1).getInterval()))
                .toList());
        return summary;
    }

}
