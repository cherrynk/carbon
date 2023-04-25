package com.example.spldemo.excel.week;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class Series {
    private String name;
    private String type;
    private List<BigDecimal> data;
    private MarkPoint markPoint;

    public static  Series buildDefault(){
        return Series.builder().type("bar").markPoint(MarkPoint.buildDefault()).build();
    }
}
