package com.community.server.controller;

import com.community.server.model.Result;
import com.community.server.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 获取统计概览数据
     */
    @GetMapping("/overview")
    public Result<Map<String, Object>> getOverview() {
        Map<String, Object> data = statisticsService.getOverviewData();
        return Result.success(data);
    }

    /**
     * 获取人员分类统计数据
     */
    @GetMapping("/category")
    public Result<Map<String, Object>> getCategoryStatistics() {
        Map<String, Object> data = statisticsService.getCategoryStatistics();
        return Result.success(data);
    }

    /**
     * 获取关怀类型统计数据
     */
    @GetMapping("/care-type")
    public Result<Map<String, Object>> getCareTypeStatistics() {
        Map<String, Object> data = statisticsService.getCareTypeStatistics();
        return Result.success(data);
    }

    /**
     * 获取月度关怀统计数据
     */
    @GetMapping("/monthly")
    public Result<Map<String, Object>> getMonthlyStatistics() {
        Map<String, Object> data = statisticsService.getMonthlyStatistics();
        return Result.success(data);
    }
} 