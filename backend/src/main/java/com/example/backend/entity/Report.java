package com.example.backend.entity;

import jakarta.persistence.*;

import java.util.Date;
@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String commentText;
    private String detectedContent; // 侮辱性词语、攻击性行为等

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    // Getters and Setters
}
