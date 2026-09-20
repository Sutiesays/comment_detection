package com.example.backend.service;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean checkPassword(User user, String rawPassword) {
        // 实现密码验证，通常会加密密码并与数据库中的加密密码比较
        return user.getPassword().equals(rawPassword);
    }

    public void updateUserLoginTime(User user) {
        user.setLastLogin(new Date());
        userRepository.save(user);
    }
}
