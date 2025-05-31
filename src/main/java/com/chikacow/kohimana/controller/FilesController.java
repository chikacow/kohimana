package com.chikacow.kohimana.controller;

import com.chikacow.kohimana.model.Files;
import com.chikacow.kohimana.service.impl.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
@Validated
@Slf4j
public class FilesController {
    private final FileService fileService;


    @PostMapping("/upload")
    public ResponseEntity<Files> uploadImage(@RequestParam("file") MultipartFile file) {

        Files theFile = fileService.saveFile(file);
        return ResponseEntity.ok(theFile);
    }
}
