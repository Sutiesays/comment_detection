package com.example.backend.controller;

import com.example.backend.entity.Report;
import com.example.backend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/generate")
    public Report generateReport(@RequestParam String commentText) {
        return reportService.createReport(commentText);
    }

    @GetMapping("/{id}")
    public Report getReportById(@PathVariable Long id) {
        return reportService.getReportById(id);
    }

    @PostMapping("/feedback")
    public void receiveFeedback(@RequestParam Long reportId, @RequestParam String feedback) {
        reportService.collectFeedback(reportId, feedback);
    }
}
