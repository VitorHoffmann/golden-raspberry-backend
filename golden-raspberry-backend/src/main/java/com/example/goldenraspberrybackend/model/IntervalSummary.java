package com.example.goldenraspberrybackend.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class IntervalSummary {
    List<AwardsInterval> min;
    List<AwardsInterval> max;

    public IntervalSummary() {
    }

}
