package com.example.demo.controller;

import com.example.demo.entity.Enrollment;
import com.example.demo.service.EnrollmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    private final EnrollmentService svc;
    public EnrollmentController(EnrollmentService svc){ this.svc = svc; }

    @PostMapping("/student/{studentId}")
    public List<Enrollment> enroll(@PathVariable Long studentId, @RequestBody List<Long> courseIds){
        return svc.enrollStudentInCourses(studentId, courseIds);
    }

    @PostMapping("/{studentId}/{courseId}/grade")
    public Enrollment recordGrade(@PathVariable Long studentId, @PathVariable Long courseId, @RequestBody Map<String,String> payload){
        String grade = payload.get("grade");
        return svc.recordGrade(studentId, courseId, grade);
    }

    @GetMapping("/student/{studentId}")
    public List<Enrollment> getStudentEnrollments(@PathVariable Long studentId){
        return svc.getStudentEnrollments(studentId);
    }

    @GetMapping("/student/{studentId}/gpa")
    public Double getGpa(@PathVariable Long studentId){ return svc.computeGPA(studentId); }
}
