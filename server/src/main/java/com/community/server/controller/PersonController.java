package com.community.server.controller;

import com.community.server.model.Person;
import com.community.server.model.Result;
import com.community.server.model.User;
import com.community.server.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    /**
     * 获取当前登录用户
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    /**
     * 获取人员列表
     */
    @GetMapping
    public Result<List<Person>> getPersons(@RequestParam(required = false) String keyword) {
        User currentUser = getCurrentUser();
        List<Person> persons = personService.findPersons(currentUser.getId(), currentUser.getRole(), keyword);
        return Result.success(persons);
    }

    /**
     * 获取人员详情
     */
    @GetMapping("/{id}")
    public Result<Person> getPersonById(@PathVariable Long id) {
        Person person = personService.findById(id);
        return Result.success(person);
    }

    /**
     * 创建人员
     */
    @PostMapping
    public Result<Person> createPerson(@Valid @RequestBody Person person) {
        User currentUser = getCurrentUser();
        Person createdPerson = personService.create(person, currentUser.getId());
        return Result.success(createdPerson);
    }

    /**
     * 更新人员
     */
    @PutMapping("/{id}")
    public Result<Person> updatePerson(@PathVariable Long id, @Valid @RequestBody Person person) {
        User currentUser = getCurrentUser();
        person.setId(id); // 确保ID正确
        Person updatedPerson = personService.update(person, currentUser.getId(), currentUser.getRole());
        return Result.success(updatedPerson);
    }

    /**
     * 删除人员
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deletePerson(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        boolean success = personService.deleteById(id, currentUser.getId(), currentUser.getRole());
        return Result.success(success);
    }
} 