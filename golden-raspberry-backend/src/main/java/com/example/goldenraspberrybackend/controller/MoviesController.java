package com.example.goldenraspberrybackend.controller;

import com.example.goldenraspberrybackend.model.IntervalSummary;
import com.example.goldenraspberrybackend.service.MoviesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies")
public class MoviesController {

    private final MoviesService service;

    public MoviesController(MoviesService service) {
        this.service = service;
    }

    @GetMapping
    public IntervalSummary getSummary() {
        return service.buildSummary();
    }
}
