package com.example.backend.service;

import com.example.backend.entity.Report;
import com.example.backend.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public Report createReport(String commentText) {
        // 生成报告的逻辑，可以结合 `DetectionService` 进行恶意评论检测
        // 例如：调用 `DetectionService` 来获取检测结果
        return new Report();
    }

    public Report getReportById(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No report found with id: " + id));
    }


    public void collectFeedback(Long reportId, String feedback) {
        // 存储用户反馈
    }

    public Map<String, Object> getAccuracyStats() {
        // 返回系统的准确率等统计数据
        Map<String, Object> stats = new HashMap<>();
        stats.put("accuracy", 0.95);
        return stats;
    }

    public Map<String, Object> getMisclassificationStats() {
        // 返回误报率等统计数据
        Map<String, Object> stats = new HashMap<>();
        stats.put("false_positive_rate", 0.1);
        return stats;
    }
    }

