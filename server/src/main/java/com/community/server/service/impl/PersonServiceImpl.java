package com.community.server.service.impl;

import com.community.server.exception.ResourceNotFoundException;
import com.community.server.exception.UnauthorizedException;
import com.community.server.mapper.PersonMapper;
import com.community.server.model.Person;
import com.community.server.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonMapper personMapper;

    @Override
    public List<Person> findPersons(Long userId, String role, String keyword) {
        if ("ADMIN".equals(role)) {
            if (keyword != null && !keyword.isEmpty()) {
                return personMapper.search(keyword, null);
            }
            return personMapper.findAll();
        } else {
            if (keyword != null && !keyword.isEmpty()) {
                return personMapper.search(keyword, userId);
            }
            return personMapper.findByCreatorId(userId);
        }
    }

    @Override
    public Person findById(Long id) {
        Person person = personMapper.findById(id);
        if (person == null) {
            throw new ResourceNotFoundException("Person", "id", id);
        }
        return person;
    }

    @Override
    public Person create(Person person, Long userId) {
        person.setCreatorId(userId);
        person.setCreateTime(new Date());
        person.setUpdateTime(new Date());
        
        personMapper.insert(person);
        return person;
    }

    @Override
    public Person update(Person person, Long userId, String role) {
        Person existingPerson = findById(person.getId());
        
        // 检查权限：只有管理员或者创建者可以修改
        if (!"ADMIN".equals(role) && !existingPerson.getCreatorId().equals(userId)) {
            throw new UnauthorizedException("无权修改此人员信息");
        }
        
        // 更新基本信息
        existingPerson.setName(person.getName());
        existingPerson.setGender(person.getGender());
        existingPerson.setAge(person.getAge());
        existingPerson.setIdCard(person.getIdCard());
        existingPerson.setPhone(person.getPhone());
        existingPerson.setCategory(person.getCategory());
        existingPerson.setAddress(person.getAddress());
        existingPerson.setRemark(person.getRemark());
        
        // 更新时间
        existingPerson.setUpdateTime(new Date());
        
        personMapper.update(existingPerson);
        return existingPerson;
    }

    @Override
    public boolean deleteById(Long id, Long userId, String role) {
        Person person = findById(id);
        
        // 检查权限：只有管理员或者创建者可以删除
        if (!"ADMIN".equals(role) && !person.getCreatorId().equals(userId)) {
            throw new UnauthorizedException("无权删除此人员信息");
        }
        
        return personMapper.deleteById(id) > 0;
    }
} 