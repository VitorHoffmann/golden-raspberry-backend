package com.example.goldenraspberrybackend.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AwardsInterval {

    String producer;
    Integer interval;
    Integer previousWin;
    Integer followingWin;

    public AwardsInterval() {

    }

}
