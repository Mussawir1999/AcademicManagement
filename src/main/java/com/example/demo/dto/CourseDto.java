package com.example.demo.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class CourseDto {
    private Long id;
    private String code;
    private String title;
    private int maxCapacity = 30;
    private Set<CourseDto> prerequisites;
    private List<EnrollmentDto> enrollments;

}
