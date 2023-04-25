package com.example.spldemo.excel.week;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class MultiCar2 {
    Legend legend;

    List<HoursX> xAxis;

    List<HoursX> yAxis;

    List<Series2> series;

    Map tooltip;

    Map toolbox;

    Map grid;

    public static MultiCar2 buildDefault(){
        List<HoursX> xAxi = new ArrayList<>();
        xAxi.add(HoursX.getDefaultHourX());
        List<HoursX> yAxi = new ArrayList<>();;
        yAxi.add(HoursX.builder().type("value").build());
        Map tp = new HashMap();
        tp.put("trigger","axis");
        Map apMap= new HashMap();
        apMap.put("type","cross");
        Map color = new HashMap();
        color.put("backgroundColor","#6a7985");
        apMap.put("label",color);
        tp.put("axisPointer",apMap);


        Map tb  = new HashMap();
        Map feature  = new HashMap();
        feature.put("saveAsImage",new HashMap());
        tb.put("feature",feature);

        Map gr= new HashMap();
        gr.put("left","3%");
        gr.put("right","4%");
        gr.put("bottom","3%");
        gr.put("containLabel",true);
        return MultiCar2.builder().tooltip(tp).grid(gr).toolbox(tb).xAxis(xAxi).yAxis(yAxi).build();
    }
}
