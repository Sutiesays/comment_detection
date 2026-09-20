package com.example.backend.controller;

import com.example.backend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/statistics")
public class StatisticsController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/accuracy")
    public Map<String, Object> getAccuracyStatistics() {
        return reportService.getAccuracyStats();
    }

    @GetMapping("/misclassifications")
    public Map<String, Object> getMisclassificationStats() {
        return reportService.getMisclassificationStats();
    }
}
