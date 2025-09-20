package com.example.demo.service;

import com.example.demo.entity.Course;
import com.example.demo.entity.Enrollment;
import com.example.demo.entity.Student;
import com.example.demo.exception.DomainException;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class EnrollmentService {
    private final StudentRepository students;
    private final CourseRepository courses;
    private final EnrollmentRepository enrollments;

    public EnrollmentService(StudentRepository students, CourseRepository courses, EnrollmentRepository enrollments){
        this.students = students; this.courses = courses; this.enrollments = enrollments;
    }

    @Transactional
    public List<Enrollment> enrollStudentInCourses(Long studentId, List<Long> courseIds){
        Student s = students.findById(studentId).orElseThrow(()->new DomainException("Student not found"));
        List<Enrollment> created = new ArrayList<>();
        for(Long courseId : courseIds){
            Course c = courses.findById(courseId).orElseThrow(()->new DomainException("Course not found: " + courseId));
            int current = enrollments.countByCourseId(c.getId());
            if(current >= c.getMaxCapacity()){
                throw new DomainException("Course " + c.getCode() + " is at capacity");
            }
            Set<Course> prereqs = c.getPrerequisites();
            if(prereqs != null && !prereqs.isEmpty()){
                for(Course req : prereqs){
                    boolean satisfied = enrollments.findByStudentIdAndCourseId(s.getId(), req.getId()).isPresent()
                            || (s.getEnrollments()!=null && s.getEnrollments().stream().anyMatch(e -> e.getCourse().getId().equals(req.getId()) && e.isCompleted()));
                    if(!satisfied) throw new DomainException("Missing prerequisite: " + req.getCode() + " for course " + c.getCode());
                }
            }
            if(enrollments.findByStudentIdAndCourseId(s.getId(), c.getId()).isPresent()){
                continue;
            }
            Enrollment e = Enrollment.builder().student(s).course(c).completed(false).build();
            created.add(enrollments.save(e));
        }
        return created;
    }

    @Transactional
    public Enrollment recordGrade(Long studentId, Long courseId, String grade){
        Enrollment e = enrollments.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(()->new DomainException("Student is not enrolled in course"));
        if(e.isCompleted()){
            throw new DomainException("Cannot update grade for a completed course");
        }
        if(!isValidGrade(grade)) throw new DomainException("Invalid grade format");
        e.setGrade(grade);
        e.setCompleted(true);
        return enrollments.save(e);
    }

    public boolean isValidGrade(String grade){
        if(grade==null) return false;
        if(grade.matches("^[A-F][+-]?$")) return true;
        try {
            double val = Double.parseDouble(grade);
            return val >= 0 && val <= 100;
        } catch(Exception ex){ return false; }
    }

    public List<Enrollment> getStudentEnrollments(Long studentId){
        return enrollments.findByStudentId(studentId);
    }
    public double computeGPA(Long studentId){
        List<Enrollment> list = getStudentEnrollments(studentId);
        double total=0; int count=0;
        for(var e: list){
            if(e.getGrade()!=null){
                Double pts = gradeToPoints(e.getGrade());
                if(pts!=null){ total += pts; count++; }
            }
        }
        return count==0? 0.0 : total/count;
    }

    private Double gradeToPoints(String g){
        if(g==null) return null;
        g = g.trim().toUpperCase();
        switch(g){
            case "A+": case "A": return 4.0;
            case "A-": return 3.7;
            case "B+": return 3.3;
            case "B": return 3.0;
            case "B-": return 2.7;
            case "C+": return 2.3;
            case "C": return 2.0;
            case "D": return 1.0;
            case "F": return 0.0;
            default:
                try {
                    double val = Double.parseDouble(g);
                    if(val >= 85) return 4.0;
                    if(val >= 70) return 3.0;
                    if(val >= 60) return 2.0;
                    if(val >= 50) return 1.0;
                    return 0.0;
                } catch(Exception ex){ return null; }
        }
    }
}
