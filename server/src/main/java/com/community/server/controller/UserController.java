package com.community.server.controller;

import com.community.server.model.Result;
import com.community.server.model.User;
import com.community.server.model.dto.UserDTO;
import com.community.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取普通用户列表
     */
    @GetMapping
    public Result<List<User>> list() {
        List<User> users = userService.findAllUsers();
        return Result.success(users);
    }

    /**
     * 创建新用户
     */
    @PostMapping
    public Result<Void> create(@RequestBody User user) {
        userService.createUser(user);
        return Result.success();
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        userService.updateUser(user);
        return Result.success();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }

    /**
     * 重置用户密码
     */
    @PostMapping("/{id}/reset-password")
    public Result<Void> resetPassword(@PathVariable Long id) {
        userService.resetPassword(id);
        return Result.success();
    }

    @PutMapping("/update-info")
    public Result<Void> updateInfo(@RequestBody User user, @AuthenticationPrincipal UserDetails userDetails) {
        user.setUsername(userDetails.getUsername());
        userService.updateUserInfo(user);
        return Result.success();
    }

    @PutMapping("/change-password")
    public Result<Void> changePassword(@RequestBody Map<String, String> params, @AuthenticationPrincipal UserDetails userDetails) {
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        userService.changePassword(userDetails.getUsername(), oldPassword, newPassword);
        return Result.success();
    }

    /**
     * 将User实体转换为UserDTO
     */
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        return dto;
    }

    /**
     * 将UserDTO转换为User实体
     */
    private User convertToEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());
        user.setRole(dto.getRole());
        return user;
    }
} 