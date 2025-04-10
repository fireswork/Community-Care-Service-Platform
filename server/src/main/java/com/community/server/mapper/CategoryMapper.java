package com.community.server.mapper;

import com.community.server.model.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CategoryMapper {
    List<Category> findAll();
    
    List<Category> findByNameOrDescription(@Param("keyword") String keyword);
    
    Category findById(Long id);
    
    int insert(Category category);
    
    int update(Category category);
    
    int deleteById(Long id);
} 