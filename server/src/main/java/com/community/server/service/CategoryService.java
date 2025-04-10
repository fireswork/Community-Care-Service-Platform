package com.community.server.service;

import com.community.server.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    
    List<Category> findByKeyword(String keyword);
    
    Category findById(Long id);
    
    Category create(Category category);
    
    Category update(Category category);
    
    boolean deleteById(Long id);
} 