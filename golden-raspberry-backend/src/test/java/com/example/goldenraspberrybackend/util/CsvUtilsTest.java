package com.example.goldenraspberrybackend.util;

import com.example.goldenraspberrybackend.model.Movie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvUtilsTest {

    @TempDir
    Path tempDir;

    @Test
    void testReadCsvWithValidContent() throws Exception {
        File tempCsv = tempDir.resolve("test-movies.csv").toFile();

        try (Writer writer = new FileWriter(tempCsv)) {
            writer.write("year;title;studios;producers;winner\n");
            writer.write("1980;Movie 1;Studio A;Producer A;yes\n");
            writer.write("1990;Movie 2;Studio B;Producer B;\n");
        }

        Path target = Path.of("src/test/resources/test-movies.csv");
        FileCopyUtils.copy(tempCsv, target.toFile());

        List<Movie> movies = CsvUtils.readCsv("test-movies.csv");

        assertEquals(2, movies.size());

        Movie movie1 = movies.get(0);
        assertEquals(1980, movie1.getYear());
        assertEquals("Movie 1", movie1.getTitle());
        assertEquals("Studio A", movie1.getStudios());
        assertEquals("Producer A", movie1.getProducers());
        assertTrue(movie1.getWinner());

        Movie movie2 = movies.get(1);
        assertEquals(1990, movie2.getYear());
        assertEquals("Movie 2", movie2.getTitle());
        assertEquals("Studio B", movie2.getStudios());
        assertEquals("Producer B", movie2.getProducers());
        assertFalse(movie2.getWinner());
    }

    @Test
    void testReadCsvWithInvalidFileName() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            CsvUtils.readCsv("nonexistent.csv");
        });

        assertTrue(exception.getMessage().contains("Error reading CSV file"));
    }
}
