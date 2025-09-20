package com.example.demo.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class ImportStatusDto {
    private Long id;
    private String filename;
    private String status;
    private int total;
    private int processed;
    private String message;
    private Instant createdAt;
    private Instant updatedAt;
}
