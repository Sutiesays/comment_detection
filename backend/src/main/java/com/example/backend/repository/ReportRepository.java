package com.example.backend.repository;

import com.example.backend.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    // 根据报告ID查找报告
    Optional<Report> findById(Long id);

    // 根据评论内容检测报告
    List<Report> findByCommentTextContaining(String keyword);

    // 其他根据需求的查询方法，如按创建时间排序，按检测类型筛选等
    List<Report> findByCreateTimeBetween(Long startTime, Long endTime);
}
