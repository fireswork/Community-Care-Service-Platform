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
            @RequestParam(required = false) String type,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        List<Record> records;
        
        if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            // 管理员可以看到所有记录
            records = recordService.getRecords(category, type);
        } else {
            // 普通用户只能看到自己创建的记录
            records = recordService.getRecordsByUser(currentUser.getId(), category, type);
        }
        
        return Result.success(records);
    }

    @GetMapping("/{id}")
    public Result<Record> getRecordById(@PathVariable Long id, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        Record record = recordService.getRecordById(id);
        
        if (record == null) {
            return Result.error("记录不存在");
        }
        
        // 检查权限：只有管理员或记录创建者可以查看
        if ("ADMIN".equalsIgnoreCase(currentUser.getRole()) || record.getCreatedBy().equals(currentUser.getId())) {
            return Result.success(record);
        } else {
            return Result.error("没有权限查看该记录");
        }
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
        User currentUser = (User) authentication.getPrincipal();
        Record existingRecord = recordService.getRecordById(id);
        
        if (existingRecord == null) {
            return Result.error("记录不存在");
        }
        
        // 检查权限：只有管理员或记录创建者可以更新
        if (!"ADMIN".equalsIgnoreCase(currentUser.getRole()) && !existingRecord.getCreatedBy().equals(currentUser.getId())) {
            return Result.error("没有权限更新该记录");
        }
        
        record.setId(id);
        record.setUpdatedBy(currentUser.getId());
        int rows = recordService.updateRecord(record);
        return rows > 0 ? Result.success() : Result.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteRecord(@PathVariable Long id, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        Record existingRecord = recordService.getRecordById(id);
        
        if (existingRecord == null) {
            return Result.error("记录不存在");
        }
        
        // 检查权限：只有管理员或记录创建者可以删除
        if (!"ADMIN".equalsIgnoreCase(currentUser.getRole()) && !existingRecord.getCreatedBy().equals(currentUser.getId())) {
            return Result.error("没有权限删除该记录");
        }
        
        int rows = recordService.deleteRecord(id);
        return rows > 0 ? Result.success() : Result.error("删除失败");
    }
} 