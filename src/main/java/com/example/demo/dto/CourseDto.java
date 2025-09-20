package com.example.demo.dto;

import com.example.demo.entity.Course;
import com.example.demo.entity.Enrollment;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class CourseDto {
    private Long id;
    private String code;
    private String title;
    private int maxCapacity = 30;
    private Set<Course> prerequisites;
    private List<Enrollment> enrollments;

}
