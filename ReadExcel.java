package com.example.spldemo.excel.week;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.example.spldemo.excel.DataPosition;
import com.example.spldemo.excel.EasyExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.StringUtils;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
@Slf4j
public class ReadExcel {
    public static void main(String[] args) throws Exception {

        writeHourDataToExcel();

        //writeElecDataToExcel();

        //writeHourDataToJson();

        //writeHourDataToJson2();


    }

    private static void writeElecDataToExcel() throws Exception{
        List<DataPosition> all = new ArrayList<>();
        String fileName = "/Users/tanlian/Desktop/周刊/1122.文件1唐纯+秦纯csc.xlsx";
        all.addAll( readFileToData2(fileName,2,2));
        all.addAll( readFileToData2(fileName,2,3));
        fileName = "/Users/tanlian/Desktop/周刊/1122.文件2唐混+秦混csc.xlsx";
        all.addAll( readFileToData2(fileName,2,2));
        all.addAll( readFileToData2(fileName,2,3));


        int i =0;
    }

    private static void writeHourDataToJson2()  throws Exception{
        String fileName1 = "/Users/tanlian/Desktop/周刊/小时碳排放-唐混.xlsx";
        String fileName2 = "/Users/tanlian/Desktop/周刊/小时碳排放-秦混.xlsx";
        String fileName3 = "/Users/tanlian/Desktop/周刊/小时碳排放-唐纯.xlsx";
        String fileName4 = "/Users/tanlian/Desktop/周刊/小时碳排放-秦纯.xlsx";

        List<Series2> seriesList = new ArrayList<>();
        MultiCar2 multiCar1 = MultiCar2.buildDefault();
        multiCar1.setSeries(seriesList);
        Legend legend = new Legend();
        List<String> legendType= new ArrayList<>();
        legend.setData(legendType);
        multiCar1.setLegend(legend);

        List<HourCarbon>  hourCarbons = new ArrayList<>();
        addHourCarbon(fileName3,"唐纯",hourCarbons);
        getSeries2("唐纯",hourCarbons,seriesList,legend,false);

        hourCarbons = new ArrayList<>();
        addHourCarbon(fileName4,"秦纯",hourCarbons);
        getSeries2("秦纯",hourCarbons,seriesList,legend,false);


        hourCarbons = new ArrayList<>();
        addHourCarbon(fileName1,"唐混",hourCarbons);
        getSeries2("唐混",hourCarbons,seriesList,legend,false);


        hourCarbons = new ArrayList<>();
        addHourCarbon(fileName2,"秦混",hourCarbons);
        getSeries2("秦混",hourCarbons,seriesList,legend,true);




        String fileName5 = "/Users/tanlian/Desktop/周刊/小时碳排放-出车树.txt";

        FileWriter writer = new FileWriter(fileName5);
        writer.write(JSONUtil.toJsonStr(multiCar1));
        writer.close();
        System.out.println(hourCarbons.size());
    }

    private static void writeHourDataToJson() throws Exception{
        String fileName1 = "/Users/tanlian/Desktop/周刊/小时碳排放-唐混.xlsx";
        String fileName2 = "/Users/tanlian/Desktop/周刊/小时碳排放-秦混.xlsx";
        String fileName3 = "/Users/tanlian/Desktop/周刊/小时碳排放-唐纯.xlsx";
        String fileName4 = "/Users/tanlian/Desktop/周刊/小时碳排放-秦纯.xlsx";

        List<Series> seriesList = new ArrayList<>();
        MultiCar1 multiCar1 = MultiCar1.buildDefault();
        multiCar1.setSeries(seriesList);
        Legend legend = new Legend();
        List<String> legendType= new ArrayList<>();
        legend.setData(legendType);
        multiCar1.setLegend(legend);
        Series series = null;
       List<HourCarbon>  hourCarbons = new ArrayList<>();
         /*addHourCarbon(fileName3,"唐纯",hourCarbons);
        series = getSeries("唐纯",hourCarbons);
        legendType.add("唐纯");
        seriesList.add(series);

        hourCarbons = new ArrayList<>();
        addHourCarbon(fileName4,"秦纯",hourCarbons);
        series = getSeries("秦纯",hourCarbons);
        legendType.add("秦纯");
        seriesList.add(series);

        hourCarbons = new ArrayList<>();
        addHourCarbon(fileName1,"唐混",hourCarbons);
        legendType.add("唐混");
        series = getSeries("唐混",hourCarbons);
        seriesList.add(series);*/

        hourCarbons = new ArrayList<>();
        addHourCarbon(fileName2,"秦混",hourCarbons);
        legendType.add("秦混");
        series = getSeries("秦混",hourCarbons);
        seriesList.add(series);



        String fileName5 = "/Users/tanlian/Desktop/周刊/小时碳排放-秦混日.txt";

        FileWriter writer = new FileWriter(fileName5);
        writer.write(JSONUtil.toJsonStr(multiCar1));
        writer.close();
        System.out.println(hourCarbons.size());
    }

    private static Series2 getSeries2(String type, List<HourCarbon> hourCarbons, List<Series2> seriesList, Legend legend, boolean isTop) {
        Series2 series = Series2.buildDefault(isTop);
        legend.getData().add(type);
        series.setName(type);
        Map<Integer, List<HourCarbon>> carbonGroupByHour = hourCarbons.stream().collect(Collectors.groupingBy(HourCarbon::getHour));
        List<BigDecimal> hoursDefault = new ArrayList<>(24);
        for(int i=0;i<24;i++)hoursDefault.add(null);
        series.setData(hoursDefault);
        setDataToSeries(carbonGroupByHour,hoursDefault);
        seriesList.add(series);
        return series;
    }

    private static Series getSeries(String type, List<HourCarbon> hourCarbons) {
        Series series = Series.buildDefault();
        series.setName(type);
        Map<Integer, List<HourCarbon>> carbonGroupByHour = hourCarbons.stream().collect(Collectors.groupingBy(HourCarbon::getHour));
        List<BigDecimal> hoursDefault = new ArrayList<>(24);
        for(int i=0;i<24;i++)hoursDefault.add(null);
        series.setData(hoursDefault);
        setDataToSeries(carbonGroupByHour,hoursDefault);
        return series;
    }

    private static void setDataToSeries(Map<Integer, List<HourCarbon>> carbonGroupByHour, List<BigDecimal> hoursDefault) {
        carbonGroupByHour.forEach((hour,carbonList)->{
            BigDecimal oneHourCarbon= getOneHourCarbon(carbonList);
            hoursDefault.set(hour,oneHourCarbon);
        });
    }

    private static BigDecimal getOneHourCarbon(List<HourCarbon> carbonList) {
        final BigDecimal[] total = {BigDecimal.ZERO};
        final BigDecimal[] num = {BigDecimal.ZERO};
        carbonList.forEach(e->{
            total[0] = total[0].add(e.getCarbon());
            num[0] = num[0].add(new BigDecimal(1));
        });
        //return num[0];
        return total[0].divide(num[0],6,RoundingMode.HALF_DOWN);
    }

    private static void addHourCarbon(String fileName, String type, List<HourCarbon> hourCarbons) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(new File(fileName));
        List<Map<Integer, String>> dataList =  EasyExcelUtils.parseCommDateStream(fileInputStream,2,0);
        dataList.forEach(e->{
            e.forEach((hour,carbon)->{
                if(!StringUtils.isEmpty(carbon))
                hourCarbons.add(HourCarbon.builder().type(type).hour(hour).carbon(new BigDecimal(carbon)).build());
            });
        });
        fileInputStream.close();
    }



    private static void writeHourDataToExcel() throws Exception{
        String[] prefixNames = new String[]{"ER","PE","BE"};
        Integer[] carbonIndexs = new Integer[]{23,22,21};
        for(int i=0;i<3;i++){
            String fileName = "/Users/tanlian/Desktop/周刊/data2/12.8.文件1唐纯+秦纯-csc给倪侃用于提取 改1.22后.xlsx";
            List<DataPosition>  dataPositionsSheet2 = readFileToData(fileName,2,2,carbonIndexs[i]);
            List<DataPosition>  dataPositionsSheet3 = readFileToData(fileName,2,3,carbonIndexs[i]);
            fileName = "/Users/tanlian/Desktop/周刊/data2/"+prefixNames[i]+"小时碳排放-唐纯.xlsx";
            writeDataToSheet(fileName,dataPositionsSheet2,0);
            fileName = "/Users/tanlian/Desktop/周刊/data2/"+prefixNames[i]+"小时碳排放-秦纯.xlsx";
            writeDataToSheet(fileName,dataPositionsSheet3,0);

            fileName = "/Users/tanlian/Desktop/周刊/data2/12.8.文件2唐混+秦混-csc给倪侃用于提取 改1.22后.xlsx";
            dataPositionsSheet2 = readFileToData(fileName,2,2,carbonIndexs[i]);
            dataPositionsSheet3 = readFileToData(fileName,2,3,carbonIndexs[i]);
            fileName = "/Users/tanlian/Desktop/周刊/data2/"+prefixNames[i]+"小时碳排放-唐混.xlsx";
            writeDataToSheet(fileName,dataPositionsSheet2,0);
            fileName = "/Users/tanlian/Desktop/周刊/data2/"+prefixNames[i]+"小时碳排放-秦混.xlsx";
            writeDataToSheet(fileName,dataPositionsSheet3,0);
        }

    }

    private static void writeDataToSheet(String fileName, List<DataPosition> dataPositions, int sheetIndex) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), new ArrayList(),new ArrayList());
        writeData(workbook.getSheetAt(sheetIndex),dataPositions);
        workbook.write(fileOutputStream);
        workbook.close();
    }

    private static void writeData(Sheet sheet, List<DataPosition> dataPositions) {
        dataPositions.forEach(e->{
            createCell(sheet,e.getRowIndex(),e.getColIndex());
            sheet.getRow(e.getRowIndex()).getCell(e.getColIndex()).setCellValue(e.getData());
        });
    }

    private static void createCell(Sheet sheet, int rowIndex, int colIndex) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) sheet.createRow(rowIndex);
        Cell cell = sheet.getRow(rowIndex).getCell(colIndex);
        if (cell == null) sheet.getRow(rowIndex).createCell(colIndex);
    }

    private static List<DataPosition> readFileToData(String fileName, int startRow, int sheetIndex, int carbonIndex) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(new File(fileName));
        List<Map<Integer, String>> dataList =  EasyExcelUtils.parseCommDateStream(fileInputStream,startRow,sheetIndex);
        List<DataPosition> dataPosition = getDataPosition(dataList,carbonIndex);
        fileInputStream.close();
        return dataPosition;
    }

    private static List<DataPosition> readFileToData2(String fileName, int startRow, int sheetIndex) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(new File(fileName));
        List<Map<Integer, String>> dataList =  EasyExcelUtils.parseCommDateStream(fileInputStream,startRow,sheetIndex);
        List<DataPosition> dataPosition = getDataPosition2(dataList);
        fileInputStream.close();
        return dataPosition;
    }

    private static List<DataPosition> getDataPosition2(List<Map<Integer, String>> dataList) {
        List<DataPosition>  dataPositions = new ArrayList<>();
        final int[] rowIndex = {2};
        dataList.forEach(e->{
            if(e.size() == 26){
                String travelStartTime = e.get(8);
                String travelEndTime = e.get(10);
                String elec = e.get(13);
                String hours = e.get(22);
                String erCarbonStr = e.get(25);
                if(!inTenSecond(travelStartTime,travelEndTime)) {
                    DateTime travelStartDate = truncateSecond(travelStartTime);
                    DateTime travelEndDate = truncateSecond(travelEndTime);
                    long between = DateUtil.between(travelEndDate, travelStartDate, DateUnit.MINUTE);
                    int startHours = travelStartDate.getHours();
                    dataPositions.add(DataPosition.builder().rowIndex(rowIndex[0]).elec(elec).minute(between).colIndex(startHours).data(erCarbonStr).build());
                    rowIndex[0] = rowIndex[0] + 1;
                }}});
        return dataPositions;
    }

    private static List<DataPosition> getDataPosition(List<Map<Integer, String>> dataList, int carbonIndex) {
        List<DataPosition>  dataPositions = new ArrayList<>();
        AtomicInteger tenSecondTotal = new AtomicInteger();
        AtomicInteger outTenSecondTotal = new AtomicInteger(0);

        final BigDecimal[] total = {BigDecimal.ZERO};
        final BigDecimal[] totalInTen = {BigDecimal.ZERO};
        AtomicInteger rowIndex1 = new AtomicInteger(1);

        dataList.forEach(e->{
            rowIndex1.getAndIncrement();
            if(e.size() == 50){
            String travelStartTime = e.get(8);
            String travelEndTime = e.get(10);
            String elec = e.get(13);
            String hours = e.get(20);
            String erCarbonStr = e.get(carbonIndex);
            if(!inTenSecond(travelStartTime,travelEndTime)) {
                outTenSecondTotal.getAndIncrement();
                DateTime travelStartDate = truncateSecond(travelStartTime);
                DateTime travelEndDate = truncateSecond(travelEndTime);
                long between = DateUtil.between(travelEndDate, travelStartDate, DateUnit.MINUTE);
                int startHours = travelStartDate.getHours();
                int startMinutes = travelStartDate.getMinutes();
                int endHours = travelEndDate.getHours();
                int endMinutes = travelEndDate.getMinutes();
                total[0] = total[0].add(new BigDecimal(erCarbonStr));
                if (Integer.valueOf(hours) <= 1) {
                    dataPositions.add(DataPosition.builder().rowIndex(rowIndex1.get()).colIndex(startHours).data(erCarbonStr).build());
                } else {
                    BigDecimal erCarbon = new BigDecimal(erCarbonStr);
                    BigDecimal avgMinCarbon = erCarbon.divide(new BigDecimal(between), 6, RoundingMode.HALF_DOWN);
                    long leftTime = between - (60 - startMinutes + endMinutes);
                    long internalHours = leftTime / 60;

                    BigDecimal startCarbon = avgMinCarbon.multiply(new BigDecimal(60 - startMinutes));
                    addToDataPosition(dataPositions, rowIndex1.get(), startHours, startCarbon.toString());

                    for (int i = 0; i < internalHours; i++) {
                        BigDecimal hourCarbon = avgMinCarbon.multiply(new BigDecimal("60"));
                        int mod = (startHours + i + 1) % 24;
                        addToDataPosition(dataPositions, rowIndex1.get(),  mod, hourCarbon.toString());
                    }

                    BigDecimal endCarbon = avgMinCarbon.multiply(new BigDecimal(endMinutes));
                    addToDataPosition(dataPositions, rowIndex1.get(), endHours, endCarbon.toString());
                }
            }else {
                totalInTen[0] = totalInTen[0].add(new BigDecimal(erCarbonStr));
                tenSecondTotal.getAndIncrement();
            }
            }else {
                log.info("e.size not equal 26,rowindex is {}", rowIndex1.get());
            }
        });

        log.info("carbonIndex为：{}",carbonIndex);

        log.info("行驶十秒外的碳总量为：{}",total[0]);
        log.info("行驶十秒内的个数为：{}",outTenSecondTotal.get());

        log.info("行驶十秒内的碳总量为：{}",totalInTen[0]);
        log.info("行驶十秒内的个数为：{}",tenSecondTotal.get());
        return dataPositions;
    }

    private static boolean inTenSecond(String travelStartTime, String travelEndTime) {
        DateTime startDate = DateUtil.parse(travelStartTime, DatePattern.NORM_DATETIME_PATTERN);
        DateTime endDate = DateUtil.parse(travelEndTime, DatePattern.NORM_DATETIME_PATTERN);
        long between = DateUtil.between(endDate, startDate, DateUnit.SECOND);
        if(between <=10) return true;
        return false;
    }

    private static void addToDataPosition(List<DataPosition> dataPositions, int rowIndex, int colIndex, String erCarbonStr) {
        if(!StringUtils.isEmpty(erCarbonStr) && BigDecimal.ZERO.compareTo(new BigDecimal(erCarbonStr)) != 0)
        dataPositions.add(DataPosition.builder().rowIndex(rowIndex).colIndex(colIndex).data(erCarbonStr).build());
    }

    private static DateTime truncateSecond(String travelStartTime) {
        return DateUtil.parse(DateUtil.format(DateUtil.parse(travelStartTime, DatePattern.NORM_DATETIME_PATTERN), DatePattern.NORM_DATETIME_MINUTE_PATTERN), DatePattern.NORM_DATETIME_MINUTE_PATTERN);
    }
}
