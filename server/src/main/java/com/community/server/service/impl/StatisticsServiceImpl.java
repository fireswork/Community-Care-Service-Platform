package com.community.server.service.impl;

import com.community.server.mapper.PersonMapper;
import com.community.server.mapper.RecordMapper;
import com.community.server.mapper.StatisticsMapper;
import com.community.server.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private RecordMapper recordMapper;

    @Override
    public Map<String, Object> getOverviewData() {
        Map<String, Object> result = new HashMap<>();

        // 获取总关怀人数
        int totalPersons = statisticsMapper.countTotalPersons();
        result.put("totalPersons", totalPersons);

        // 获取本月新增人数
        int newPersonsThisMonth = statisticsMapper.countNewPersonsThisMonth();
        result.put("newPersonsThisMonth", newPersonsThisMonth);

        // 获取本月关怀次数
        int careCountThisMonth = statisticsMapper.countCareRecordsThisMonth();
        result.put("careCountThisMonth", careCountThisMonth);

        // 计算关怀覆盖率（有记录的人数/总人数）
        double coverageRate = 0.0;
        if (totalPersons > 0) {
            int personsWithCare = statisticsMapper.countPersonsWithCare();
            coverageRate = (double) personsWithCare / totalPersons * 100;
        }
        result.put("coverageRate", Math.round(coverageRate * 10) / 10.0); // 保留一位小数

        return result;
    }

    @Override
    public Map<String, Object> getCategoryStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        // 获取各分类人数统计
        List<Map<String, Object>> categoryStats = statisticsMapper.countPersonsByCategory();
        
        List<Map<String, Object>> seriesData = new ArrayList<>();
        for (Map<String, Object> stat : categoryStats) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", stat.get("category"));
            item.put("value", stat.get("count"));
            seriesData.add(item);
        }
        
        result.put("seriesData", seriesData);
        return result;
    }

    @Override
    public Map<String, Object> getCareTypeStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        // 定义关怀类型
        List<String> types = new ArrayList<>();
        types.add("走访");
        types.add("电话慰问");
        types.add("物资帮助");
        types.add("医疗救助");
        
        // 获取各类型关怀次数统计
        List<Map<String, Object>> typeStats = statisticsMapper.countRecordsByType();
        
        // 处理数据
        List<String> xAxisData = new ArrayList<>();
        List<Integer> seriesData = new ArrayList<>();
        
        for (String type : types) {
            xAxisData.add(type);
            
            // 查找该类型的统计数据
            Integer count = 0;
            for (Map<String, Object> stat : typeStats) {
                if (type.equals(stat.get("type"))) {
                    count = ((Number) stat.get("count")).intValue();
                    break;
                }
            }
            
            seriesData.add(count);
        }
        
        result.put("xAxisData", xAxisData);
        result.put("seriesData", seriesData);
        return result;
    }

    @Override
    public Map<String, Object> getMonthlyStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        // 获取近12个月的日期标签
        List<String> months = getLast12Months();
        result.put("xAxisData", months);
        
        // 获取各月份关怀次数
        List<Map<String, Object>> monthlyStats = statisticsMapper.countRecordsByMonth();
        
        // 处理数据
        List<Integer> seriesData = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        
        for (String month : months) {
            Integer count = 0;
            for (Map<String, Object> stat : monthlyStats) {
                String statMonth = (String) stat.get("month");
                if (month.equals(statMonth)) {
                    count = ((Number) stat.get("count")).intValue();
                    break;
                }
            }
            seriesData.add(count);
        }
        
        result.put("seriesData", seriesData);
        return result;
    }
    
    /**
     * 获取近12个月的月份标签（格式：yyyy-MM）
     */
    private List<String> getLast12Months() {
        List<String> months = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        
        // 计算近12个月的日期标签（包括当月）
        LocalDate currentDate = LocalDate.now();
        for (int i = 11; i >= 0; i--) {
            YearMonth month = YearMonth.from(currentDate.minusMonths(i));
            months.add(month.format(formatter));
        }
        
        return months;
    }
} 