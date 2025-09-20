package com.example.demo.service;

import com.example.demo.entity.Course;
import com.example.demo.exception.DomainException;
import com.example.demo.repository.CourseRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository repo;
    public CourseService(CourseRepository repo){ this.repo = repo; }

    public Page<Course> list(int page, int size){
        return repo.findAll(PageRequest.of(page,size, Sort.by("code")));
    }

    public Course create(Course c){
        if(c.getCode()!=null && repo.findByCode(c.getCode()).isPresent()){
            throw new DomainException("Course code must be unique");
        }
        return repo.save(c);
    }

    public Course get(Long id){
        return repo.findById(id).orElseThrow(()->new DomainException("Course not found"));
    }

    @Transactional
    public Course update(Long id, Course update){
        Course existing = get(id);
        existing.setTitle(update.getTitle());
        existing.setMaxCapacity(update.getMaxCapacity());
        existing.setPrerequisites(update.getPrerequisites());
        return repo.save(existing);
    }

    @Transactional
    public void delete(Long id){
        Course c = get(id);
        if(c.getEnrollments()!=null && !c.getEnrollments().isEmpty()){
            throw new DomainException("Cannot delete course with enrollments");
        }
        repo.delete(c);
    }

    public Optional<Course> findByCode(String code) {
        return repo.findByCode(code);
    }
}
