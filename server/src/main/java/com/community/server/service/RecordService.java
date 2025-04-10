package com.community.server.service;

import com.community.server.mapper.RecordMapper;
import com.community.server.model.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordService {
    @Autowired
    private RecordMapper recordMapper;

    public List<Record> getRecords(String category, String type) {
        return recordMapper.selectAll(category, type);
    }

    public Record getRecordById(Long id) {
        return recordMapper.selectById(id);
    }

    public int createRecord(Record record) {
        return recordMapper.insert(record);
    }

    public int updateRecord(Record record) {
        return recordMapper.update(record);
    }

    public int deleteRecord(Long id) {
        return recordMapper.deleteById(id);
    }
} 