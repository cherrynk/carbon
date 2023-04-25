package com.example.spldemo.excel.week;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class HoursX {
    private String type;
    private String[] data;
    Boolean boundaryGap;

    public static HoursX getDefaultHourX(){
        return HoursX.builder().type("category").data(new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23"}).build();
    }
}
