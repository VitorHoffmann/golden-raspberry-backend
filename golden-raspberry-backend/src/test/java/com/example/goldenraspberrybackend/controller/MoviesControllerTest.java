package com.example.goldenraspberrybackend.controller;

import com.example.goldenraspberrybackend.model.AwardsInterval;
import com.example.goldenraspberrybackend.model.IntervalSummary;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MoviesControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnIntervalSummaryBasedOnCsvDataset() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/movies")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        IntervalSummary summary = objectMapper.readValue(result.getResponse().getContentAsString(), IntervalSummary.class);

        List<AwardsInterval> expectedMin = new ArrayList<>();
        expectedMin.add(new AwardsInterval("Joel Silver", 1, 1990, 1991));

        List<AwardsInterval> expectedMax = new ArrayList<>();
        expectedMax.add(new AwardsInterval("Matthew Vaughn", 13, 2002, 2015));


        IntervalSummary expected = new IntervalSummary();
        expected.setMin(expectedMin);
        expected.setMax(expectedMax);

        assertThat(summary).usingRecursiveComparison().isEqualTo(expected);
    }
}