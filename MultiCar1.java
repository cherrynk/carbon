package com.example.spldemo.excel.week;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class MultiCar1 {
    Legend legend;

    List<HoursX> xAxis;

    List<HoursX> yAxis;

    List<Series> series;

    public static MultiCar1 buildDefault(){
        List<HoursX> xAxi = new ArrayList<>();
        xAxi.add(HoursX.getDefaultHourX());
        List<HoursX> yAxi = new ArrayList<>();;
        yAxi.add(HoursX.builder().type("value").build());
        return MultiCar1.builder().xAxis(xAxi).yAxis(yAxi).build();
    }
}
