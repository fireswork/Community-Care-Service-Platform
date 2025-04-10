package com.community.server.controller;

import com.community.server.common.Result;
import com.community.server.model.Category;
import com.community.server.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping
    public Result<List<Category>> findAll(@RequestParam(required = false) String keyword) {
        List<Category> categories;
        if (keyword != null && !keyword.trim().isEmpty()) {
            categories = categoryService.findByKeyword(keyword);
        } else {
            categories = categoryService.findAll();
        }
        return Result.success(categories);
    }
    
    @GetMapping("/{id}")
    public Result<Category> findById(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        if (category == null) {
            return Result.error("分类不存在");
        }
        return Result.success(category);
    }
    
    @PostMapping
    public Result<Category> create(@RequestBody Category category) {
        Category created = categoryService.create(category);
        return Result.success(created);
    }
    
    @PutMapping("/{id}")
    public Result<Category> update(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        Category updated = categoryService.update(category);
        if (updated == null) {
            return Result.error("分类不存在");
        }
        return Result.success(updated);
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        boolean success = categoryService.deleteById(id);
        if (!success) {
            return Result.error("分类不存在或删除失败");
        }
        return Result.success();
    }
} 