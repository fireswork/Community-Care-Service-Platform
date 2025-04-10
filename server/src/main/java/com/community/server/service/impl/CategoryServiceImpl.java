package com.community.server.service.impl;

import com.community.server.mapper.CategoryMapper;
import com.community.server.model.Category;
import com.community.server.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    
    @Autowired
    private CategoryMapper categoryMapper;
    
    @Override
    public List<Category> findAll() {
        return categoryMapper.findAll();
    }
    
    @Override
    public List<Category> findByKeyword(String keyword) {
        return categoryMapper.findByNameOrDescription(keyword);
    }
    
    @Override
    public Category findById(Long id) {
        return categoryMapper.findById(id);
    }
    
    @Override
    public Category create(Category category) {
        categoryMapper.insert(category);
        return category;
    }
    
    @Override
    public Category update(Category category) {
        categoryMapper.update(category);
        return categoryMapper.findById(category.getId());
    }
    
    @Override
    public boolean deleteById(Long id) {
        return categoryMapper.deleteById(id) > 0;
    }
} 