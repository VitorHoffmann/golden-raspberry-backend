package com.example.goldenraspberrybackend.controller;

import com.example.goldenraspberrybackend.model.IntervalSummary;
import com.example.goldenraspberrybackend.service.MoviesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MoviesController.class)
public class MoviesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MoviesService moviesService;

    private IntervalSummary mockSummary;

    @BeforeEach
    void setUp() {
        mockSummary = new IntervalSummary();
        mockSummary.setMin(List.of());
        mockSummary.setMax(List.of());
    }

    @Test
    void shouldReturnIntervalSummarySuccessfully() throws Exception {
        when(moviesService.buildSummary()).thenReturn(mockSummary);

        mockMvc.perform(get("/api/movies")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.min").exists())
                .andExpect(jsonPath("$.max").exists());
    }
}
