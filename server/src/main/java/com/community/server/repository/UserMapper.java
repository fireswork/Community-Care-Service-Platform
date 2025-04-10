package com.community.server.repository;

import com.community.server.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User findByUsername(@Param("username") String username);
    
    int insert(User user);
    
    int update(User user);
    
    int deleteById(@Param("id") Long id);
} 