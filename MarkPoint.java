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
public class MarkPoint {
   private List<Map> data;

   public static MarkPoint buildDefault(){
      List<Map> list = new ArrayList<>();
      Map<String,String> max = new HashMap<>();
      max.put("type","max");max.put("name","Max");
      Map<String,String> min = new HashMap<>();
      min.put("type","min");min.put("name","Min");
      list.add(max);
      list.add(min);
      return MarkPoint.builder().data(list).build();
   }
}
