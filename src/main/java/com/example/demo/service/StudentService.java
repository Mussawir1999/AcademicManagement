package com.example.demo.service;

import com.example.demo.entity.Student;
import com.example.demo.exception.DomainException;
import com.example.demo.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository repo;
    public StudentService(StudentRepository repo){ this.repo = repo; }

    public Page<Student> list(int page, int size){
        return repo.findAll(PageRequest.of(page,size, Sort.by("lastName")));
    }

    public Student create(Student s){
        if(s.getEmail()!=null && repo.findByEmail(s.getEmail()).isPresent()){
            throw new DomainException("Email already used");
        }
        return repo.save(s);
    }

    public Student get(Long id){
        return repo.findById(id).orElseThrow(()->new DomainException("Student not found"));
    }

    @Transactional
    public Student update(Long id, Student update){
        Student existing = get(id);
        existing.setFirstName(update.getFirstName());
        existing.setLastName(update.getLastName());
        existing.setEmail(update.getEmail());
        return repo.save(existing);
    }

    @Transactional
    public void delete(Long id){
        Student s = get(id);
        boolean hasActive = s.getEnrollments()!=null && s.getEnrollments().stream().anyMatch(e->!e.isCompleted());
        if(hasActive) throw new DomainException("Cannot delete student with active enrollments");
        repo.delete(s);
    }

    public Optional<Student> findByEmail(String email) {
        return repo.findByEmail(email);
    }
}
