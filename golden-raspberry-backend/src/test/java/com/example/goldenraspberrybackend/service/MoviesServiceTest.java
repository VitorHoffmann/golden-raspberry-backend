package com.example.goldenraspberrybackend.service;

import com.example.goldenraspberrybackend.model.AwardsInterval;
import com.example.goldenraspberrybackend.model.IntervalSummary;
import com.example.goldenraspberrybackend.model.Movie;
import com.example.goldenraspberrybackend.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MoviesServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    @Spy
    private MoviesService moviesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        doNothing().when(moviesService).initDatabase();
    }

    @Test
    void testGetAllMovies() {
        List<Movie> mockMovies = List.of(
                new Movie(1980, "Movie 1", "Studio A","Producer A", true),
                new Movie(1990, "Movie 2", "Studio B", "Producer B", false)
        );

        when(movieRepository.findAll()).thenReturn(mockMovies);

        List<Movie> result = moviesService.getAllMovies();

        assertEquals(2, result.size());
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void testBuildSummaryReturnsCorrectIntervals() {
        List<Movie> mockMovies = List.of(
                new Movie(1980, "Movie 1", "Studio A", "Producer X", true),
                new Movie(1982, "Movie 2", "Studio A", "Producer X", true),
                new Movie(1990, "Movie 3", "Studio A", "Producer X", true),
                new Movie(2001, "Movie 4", "Studio B", "Producer Y", true),
                new Movie(2004, "Movie 5", "Studio B", "Producer Y", true)
        );

        when(movieRepository.findAll()).thenReturn(mockMovies);

        IntervalSummary summary = moviesService.buildSummary();

        List<AwardsInterval> min = summary.getMin();
        List<AwardsInterval> max = summary.getMax();

        assertNotNull(min);
        assertNotNull(max);

        assertTrue(min.stream().anyMatch(i -> i.getInterval() == 2));
        assertTrue(max.stream().anyMatch(i -> i.getInterval() == 8));

        assertTrue(min.stream().anyMatch(i -> i.getProducer().equals("Producer X")));
        assertTrue(max.stream().anyMatch(i -> i.getProducer().equals("Producer X")));
    }

    @Test
    void testBuildSummaryWithSingleWinShouldNotBreak() {
        List<Movie> mockMovies = List.of(
                new Movie(1980, "Movie 1", "Studio C", "Producer Z", true)
        );

        when(movieRepository.findAll()).thenReturn(mockMovies);

        IntervalSummary summary = moviesService.buildSummary();

        assertTrue(summary.getMin().isEmpty() || summary.getMin().get(0).getInterval() == 300);
        assertTrue(summary.getMax().isEmpty() || summary.getMax().get(0).getInterval() == 300);
    }
}
