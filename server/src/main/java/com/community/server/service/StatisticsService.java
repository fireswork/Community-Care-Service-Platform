package com.community.server.service;

import java.util.Map;

public interface StatisticsService {
    
    /**
     * 获取统计概览数据
     * 包括总关怀人数、本月新增、本月关怀次数、关怀覆盖率
     */
    Map<String, Object> getOverviewData();
    
    /**
     * 获取人员分类统计数据
     * 返回各分类的人数统计
     */
    Map<String, Object> getCategoryStatistics();
    
    /**
     * 获取关怀类型统计数据
     * 返回各关怀类型的次数统计
     */
    Map<String, Object> getCareTypeStatistics();
    
    /**
     * 获取月度关怀统计数据
     * 返回近12个月的关怀记录数量
     */
    Map<String, Object> getMonthlyStatistics();
} 