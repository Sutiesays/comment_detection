package com.example.backend.controller;

import com.example.backend.entity.User;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> login(@RequestParam String username, @RequestParam String password) {
        User user = userService.findUserByUsername(username);
        Map<String, Object> response = new HashMap<>();

        if (user != null && userService.checkPassword(user, password)) {
            userService.updateUserLoginTime(user);
            response.put("status", "success");
            response.put("role", user.getRole());
        } else {
            response.put("status", "failure");
            response.put("message", "用户名或密码错误");
        }

        return response;
    }
}
