package com.community.server.service;

import com.community.server.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    /**
     * 用户注册
     */
    void register(User user);

    /**
     * 获取所有普通用户列表
     */
    List<User> findAllUsers();

    /**
     * 创建新用户
     */
    void createUser(User user);

    /**
     * 更新用户信息
     */
    void updateUser(User user);

    /**
     * 删除用户
     */
    void deleteUser(Long id);

    /**
     * 重置用户密码
     */
    void resetPassword(Long id);

    /**
     * 更新用户信息
     */
    void updateUserInfo(User user);

    /**
     * 修改用户密码
     */
    void changePassword(String username, String oldPassword, String newPassword);
}