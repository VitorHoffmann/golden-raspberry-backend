package com.example.goldenraspberrybackend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AwardsInterval {

    String producer;
    Integer interval;
    Integer previousWin;
    Integer followingWin;

}
