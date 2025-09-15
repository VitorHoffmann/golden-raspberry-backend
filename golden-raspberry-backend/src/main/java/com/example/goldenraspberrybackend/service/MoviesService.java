package com.example.goldenraspberrybackend.service;

import com.example.goldenraspberrybackend.model.AwardsInterval;
import com.example.goldenraspberrybackend.model.IntervalSummary;
import com.example.goldenraspberrybackend.model.Movie;
import com.example.goldenraspberrybackend.repository.MovieRepository;
import com.example.goldenraspberrybackend.util.CsvUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MoviesService {

    private final MovieRepository movieRepository;

    public MoviesService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
        readMovies();
    }

    public void readMovies() {
        List<Movie> records = CsvUtils.readCsv("movielist.csv");
        movieRepository.saveAll(records);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public IntervalSummary buildSummary () {
        IntervalSummary summary = new IntervalSummary();
        List<Movie> movies = getAllMovies();
        List<AwardsInterval> intervalList = new ArrayList<>();
        //separa uma lista apenas com os produtores dos filmes
        List<String> producersAux = movies.stream().map(Movie::getProducers).distinct().toList();
        List<String> producers = new ArrayList<>();
        producersAux.forEach(producer -> {
            String[] producerAux = producer.split(" and ");
            for (int i = 0; i < producerAux.length; i++) {
                String[] producerAux2 = producer.split(", ");
                for (int j = 0; j < producerAux2.length ; j++) {
                    if (!producers.contains(producerAux2[j])) {
                        producers.add(producerAux2[j]);
                    }
                }
            }
        });
        producers.forEach(producer -> {
            AwardsInterval awardsIntervalShortest = new AwardsInterval();
            AwardsInterval awardsIntervalLongest = new AwardsInterval();
            awardsIntervalShortest.setProducer(producer);
            awardsIntervalLongest.setProducer(producer);
            //separa a lista de filmes premiados por produtor
            List<Movie> moviesByProducer = movies.stream()
                    .filter(m -> m.getProducers().toUpperCase().contains(producer.toUpperCase()) && m.getWinner().equals(true))
                    .sorted(Comparator.comparingInt(Movie::getYear))
                    .toList();
            int shortestInterval = 300;
            int longestInterval = 0;
            int previousWinShortest = 0;
            int previousWinLongest = 0;
            int followingWinShortest = 0;
            int followingWinLongest = 0;
            //compara o menor e o maior periodo entre duas vitorias
            for (int i = 0; i < moviesByProducer.size() - 1; i++) {
                Movie movie1 = moviesByProducer.get(i);
                Movie movie2 = moviesByProducer.get(i + 1);
                int interval = movie2.getYear() - movie1.getYear();
                if (interval < shortestInterval) {
                    shortestInterval = interval;
                    previousWinShortest = movie1.getYear();
                    followingWinShortest = movie2.getYear();
                }
                if (interval > longestInterval) {
                    longestInterval = interval;
                    previousWinLongest = movie1.getYear();
                    followingWinLongest = movie2.getYear();
                }
            }

            // adiciona o menor intervalo
            awardsIntervalShortest.setInterval(shortestInterval);
            awardsIntervalShortest.setPreviousWin(previousWinShortest);
            awardsIntervalShortest.setFollowingWin(followingWinShortest);
            intervalList.add(awardsIntervalShortest);

            // caso tenha um intervalo maior adiciona
            if (longestInterval > 0 && previousWinLongest != previousWinShortest) {
                awardsIntervalLongest.setInterval(longestInterval);
                awardsIntervalLongest.setPreviousWin(previousWinLongest);
                awardsIntervalLongest.setFollowingWin(followingWinLongest);
                intervalList.add(awardsIntervalLongest);
            }
        });
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
