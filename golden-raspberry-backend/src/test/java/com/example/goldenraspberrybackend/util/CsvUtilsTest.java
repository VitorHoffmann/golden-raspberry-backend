package com.example.goldenraspberrybackend.util;

import com.example.goldenraspberrybackend.model.Movie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvUtilsTest {

    @TempDir
    Path tempDir;

    @Test
    void testReadCsvWithValidContent() throws Exception {

        List<Movie> movies = CsvUtils.readCsv("testlist.csv");
        assertEquals(3, movies.size());

        Movie movie1 = movies.get(0);
        assertEquals(2001, movie1.getYear());
        assertEquals("Movie 1", movie1.getTitle());
        assertEquals("Studio A", movie1.getStudios());
        assertEquals("Producer A", movie1.getProducers());
        assertTrue(movie1.getWinner());

        Movie movie2 = movies.get(1);
        assertEquals(2005, movie2.getYear());
        assertEquals("Movie 2", movie2.getTitle());
        assertEquals("Studio B", movie2.getStudios());
        assertEquals("Producer B", movie2.getProducers());
        assertFalse(movie2.getWinner());

        Movie movie3 = movies.get(2);
        assertEquals(2010, movie3.getYear());
        assertEquals("Movie 3", movie3.getTitle());
        assertEquals("Studio C", movie3.getStudios());
        assertEquals("Producer C", movie3.getProducers());
        assertTrue(movie1.getWinner());
    }

    @Test
    void testReadCsvWithInvalidFileName() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            CsvUtils.readCsv("nonexistent.csv");
        });

        assertTrue(exception.getMessage().contains("Error reading CSV file"));
    }
}
