package com.example.demo.dto;

import com.example.demo.entity.Enrollment;
import lombok.Data;

import java.util.List;

@Data
public class StudentDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<Enrollment> enrollments;

}
