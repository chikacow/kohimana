package com.chikacow.kohimana.controller;

import com.chikacow.kohimana.service.impl.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final MinioService minioService;

    @PostMapping("/api/v1/admin/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String url = minioService.uploadFile(file);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed: " + e.getMessage());
        }
    }

    @GetMapping("api/v1/v3/shibal")
    public String checkSCH() {

        return SecurityContextHolder.getContext().getAuthentication().getName() == null ? "null kia" : SecurityContextHolder.getContext().getAuthentication().getName() ;
    }

    @GetMapping("/v4/shibal")
    public String shibalStuck() {

        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public String testMiniO() {
        return null;
    }

}
