package com.example.demo.dto;

import com.example.demo.entity.Course;
import com.example.demo.entity.Student;
import lombok.Data;

@Data
public class EnrollmentDto {

    private Long id;
    private StudentDto student;
    private CourseDto course;
    private String grade;
    private boolean completed = false;

}
