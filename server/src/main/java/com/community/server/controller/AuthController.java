package com.community.server.controller;

import com.community.server.common.Result;
import com.community.server.model.User;
import com.community.server.security.JwtTokenUtil;
import com.community.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin  // 允许跨域请求
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User user) {
        // 进行身份验证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        
        // 设置认证信息
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 生成JWT token
        String token = jwtTokenUtil.generateToken((User) authentication.getPrincipal());
        
        // 构建响应
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", authentication.getPrincipal());
        
        return Result.success(response);
    }

    @PostMapping("/register")
    public Result<Void> register(@RequestBody User user) {
        userService.register(user);
        return Result.success();
    }
} 