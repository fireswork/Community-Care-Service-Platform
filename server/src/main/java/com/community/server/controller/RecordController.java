package com.community.server.controller;

import com.community.server.model.Record;
import com.community.server.model.Result;
import com.community.server.model.User;
import com.community.server.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/records")
public class RecordController {
    @Autowired
    private RecordService recordService;

    @GetMapping
    public Result<List<Record>> getRecords(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type) {
        List<Record> records = recordService.getRecords(category, type);
        return Result.success(records);
    }

    @GetMapping("/{id}")
    public Result<Record> getRecordById(@PathVariable Long id) {
        Record record = recordService.getRecordById(id);
        return record != null ? Result.success(record) : Result.error("记录不存在");
    }

    @PostMapping
    public Result<Void> createRecord(@RequestBody Record record, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        record.setCreatedBy(user.getId());
        record.setUpdatedBy(user.getId());
        int rows = recordService.createRecord(record);
        return rows > 0 ? Result.success() : Result.error("创建失败");
    }

    @PutMapping("/{id}")
    public Result<Void> updateRecord(@PathVariable Long id, @RequestBody Record record, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        record.setId(id);
        record.setUpdatedBy(user.getId());
        int rows = recordService.updateRecord(record);
        return rows > 0 ? Result.success() : Result.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteRecord(@PathVariable Long id) {
        int rows = recordService.deleteRecord(id);
        return rows > 0 ? Result.success() : Result.error("删除失败");
    }
} 