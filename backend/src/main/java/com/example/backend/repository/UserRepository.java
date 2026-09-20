package com.example.backend.repository;

import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 根据用户名查找用户
    User findByUsername(String username);

    // 可以根据需求添加更多的查询方法，例如按角色查找等
    // List<User> findByRole(String role);
}
