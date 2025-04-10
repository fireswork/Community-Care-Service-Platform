package com.community.server.mapper;

import com.community.server.model.Record;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RecordMapper {
    List<Record> selectAll(@Param("category") String category, @Param("type") String type);
    
    Record selectById(@Param("id") Long id);
    
    int insert(Record record);
    
    int update(Record record);
    
    int deleteById(@Param("id") Long id);
} 