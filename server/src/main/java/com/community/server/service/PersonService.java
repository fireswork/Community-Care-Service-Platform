package com.community.server.service;

import com.community.server.model.Person;

import java.util.List;

public interface PersonService {
    /**
     * 根据用户角色和ID获取人员列表
     * 如果是管理员，返回所有人员
     * 如果是普通用户，只返回该用户创建的人员
     */
    List<Person> findPersons(Long userId, String role, String keyword);
    
    /**
     * 根据ID查询人员信息
     */
    Person findById(Long id);
    
    /**
     * 创建人员信息
     */
    Person create(Person person, Long userId);
    
    /**
     * 更新人员信息
     * 需要验证当前用户是否有权限更新
     */
    Person update(Person person, Long userId, String role);
    
    /**
     * 删除人员信息
     * 需要验证当前用户是否有权限删除
     */
    boolean deleteById(Long id, Long userId, String role);
} 