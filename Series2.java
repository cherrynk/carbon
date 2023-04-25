package com.example.spldemo.excel.week;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class Series2 {
    private String name;
    private String type;
    private List<BigDecimal> data;
    private String stack;
    private Map areaStyle;
    private Map emphasis;
    private Map label;


    public static Series2 buildDefault(boolean isTop){
        Map em = new HashMap<>();
        em.put("focus","series");
        Series2 series2 = Series2.builder().type("line").stack("Total").areaStyle(new HashMap<>()).emphasis(em).build();
        if(isTop){
            Map la = new HashMap();
            la.put("show",true);
            la.put("position","top");
            series2.setLabel(la);
        }
        return series2;
    }
}
