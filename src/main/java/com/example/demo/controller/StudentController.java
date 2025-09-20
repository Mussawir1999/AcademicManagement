package com.example.demo.controller;

import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@Validated
public class StudentController {
    private final StudentService svc;
    public StudentController(StudentService svc){ this.svc = svc; }

    @GetMapping
    public Page<Student> list(@RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="20") int size){
        return svc.list(page, size);
    }

    @PostMapping
    public ResponseEntity<Student> create(@Valid @RequestBody Student s){
        return ResponseEntity.ok(svc.create(s));
    }

    @GetMapping("/{id}")
    public Student get(@PathVariable Long id){ return svc.get(id); }

    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @RequestBody @Valid Student s){ return svc.update(id, s); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){ svc.delete(id); return ResponseEntity.noContent().build(); }
}
