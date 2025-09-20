package com.example.demo.controller;

import com.example.demo.entity.Course;
import com.example.demo.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService svc;
    public CourseController(CourseService svc){ this.svc = svc; }

    @GetMapping
    public Page<Course> list(@RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="20") int size){
        return svc.list(page,size);
    }

    @PostMapping
    public Course create(@Valid @RequestBody Course c){ return svc.create(c); }

    @GetMapping("/{id}")
    public Course get(@PathVariable Long id){ return svc.get(id); }

    @PutMapping("/{id}")
    public Course update(@PathVariable Long id, @Valid @RequestBody Course c){ return svc.update(id,c); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){ svc.delete(id); }
}
