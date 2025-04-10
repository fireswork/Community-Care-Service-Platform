package com.community.server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StatisticsMapper {
    
    /**
     * 统计总人数
     */
    int countTotalPersons();
    
    /**
     * 统计本月新增人数
     */
    int countNewPersonsThisMonth();
    
    /**
     * 统计本月关怀记录数
     */
    int countCareRecordsThisMonth();
    
    /**
     * 统计有关怀记录的人数
     */
    int countPersonsWithCare();
    
    /**
     * 按分类统计人数
     */
    List<Map<String, Object>> countPersonsByCategory();
    
    /**
     * 按类型统计关怀记录数
     */
    List<Map<String, Object>> countRecordsByType();
    
    /**
     * 按月份统计关怀记录数
     */
    List<Map<String, Object>> countRecordsByMonth();
} 