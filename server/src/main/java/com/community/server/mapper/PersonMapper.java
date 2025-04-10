package com.community.server.mapper;

import com.community.server.model.Person;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PersonMapper {
    /**
     * 查询所有人员信息
     */
    List<Person> findAll();
    
    /**
     * 根据创建者ID查询人员信息
     */
    List<Person> findByCreatorId(@Param("creatorId") Long creatorId);
    
    /**
     * 根据关键词搜索，并区分是否过滤创建者
     */
    List<Person> search(@Param("keyword") String keyword, @Param("creatorId") Long creatorId);
    
    /**
     * 根据ID查询人员信息
     */
    Person findById(Long id);
    
    /**
     * 新增人员信息
     */
    int insert(Person person);
    
    /**
     * 更新人员信息
     */
    int update(Person person);
    
    /**
     * 删除人员信息
     */
    int deleteById(Long id);
} 