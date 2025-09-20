package com.example.demo.controller;

import com.example.demo.entity.ImportJob;
import com.example.demo.service.ImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/imports")
public class ImportController {
    private final ImportService importService;
    public ImportController(ImportService importService){ this.importService = importService; }

    @PostMapping("/upload")
    public ResponseEntity<ImportJob> upload(@RequestParam("file") MultipartFile file){
        ImportJob job = importService.createJob(file.getOriginalFilename());
        CompletableFuture<Void> f = importService.runImport(job.getId(), file);
        return ResponseEntity.accepted().body(job);
    }

    @GetMapping("/{id}")
    public ImportJob status(@PathVariable Long id){
        return importService.getJob(id);
    }
}