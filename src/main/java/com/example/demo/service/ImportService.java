package com.example.demo.service;

import com.example.demo.entity.Course;
import com.example.demo.entity.ImportJob;
import com.example.demo.entity.Student;
import com.example.demo.exception.DomainException;
import com.example.demo.repository.ImportJobRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class ImportService {
    private final ImportJobRepository importJobRepo;
    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    public ImportService(ImportJobRepository importJobRepo, StudentService studentService,
                         CourseService courseService, EnrollmentService enrollmentService){
        this.importJobRepo = importJobRepo;
        this.studentService = studentService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
    }

    public ImportJob createJob(String filename){
        ImportJob job = ImportJob.builder()
                .filename(filename)
                .status("PENDING")
                .createdAt(Instant.now()).updatedAt(Instant.now())
                .total(0).processed(0).build();
        return importJobRepo.save(job);
    }

    public ImportJob getJob(Long id){ return importJobRepo.findById(id).orElseThrow(()->new DomainException("Job not found")); }

    @Async
    public CompletableFuture<Void> runImport(Long jobId, MultipartFile file){
        ImportJob job = importJobRepo.findById(jobId).orElseThrow();
        job.setStatus("RUNNING"); job.setUpdatedAt(Instant.now()); importJobRepo.save(job);

        try(BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))){
            String line; int processed = 0;
            while((line = br.readLine()) != null){
                if(line.isBlank()) continue;
                String[] parts = line.split(",");
                try {
                    switch(parts[0].toUpperCase()){
                        case "STUDENT":
                            Student s = Student.builder().firstName(parts[1]).lastName(parts[2]).email(parts[3]).build();
                            studentService.create(s);
                            break;
                        case "COURSE":
                            Course c = Course.builder().code(parts[1]).title(parts[2]).maxCapacity(Integer.parseInt(parts[3])).build();
                            courseService.create(c);
                            break;
                        case "ENROLL":
                            Optional<Student> student = studentService.findByEmail(parts[1]);
                            Optional<Course> course = courseService.findByCode(parts[2]);
                            if(student.isPresent() && course.isPresent()){
                                enrollmentService.enrollStudentInCourses(student.get().getId(), java.util.List.of(course.get().getId()));
                            }
                            break;
                        default:
                    }
                } catch(Exception ex){
                    log.info("An Error occurred. Reason: {}", ex);
                }
                processed++;
                job.setProcessed(processed);
                job.setUpdatedAt(Instant.now());
                importJobRepo.save(job);
            }
            job.setStatus("COMPLETED"); job.setUpdatedAt(Instant.now());
            importJobRepo.save(job);
        } catch(Exception ex){
            job.setStatus("FAILED"); job.setMessage(ex.getMessage()); job.setUpdatedAt(Instant.now()); importJobRepo.save(job);
        }
        return CompletableFuture.completedFuture(null);
    }
}
