package com.chikacow.kohimana.service;

import com.chikacow.kohimana.model.Files;
import com.chikacow.kohimana.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final MinioService minioService;

    public Files saveFile(MultipartFile file) {
        String cloudUrl = minioService.uploadFile(file);
        String localUrl = file.getOriginalFilename();
        String type = file.getContentType();

        Files files = Files.builder()
                .localUrl(localUrl)
                .cloudUrl(cloudUrl)
                .fileType(type)
                .build();

        Files savedFile = fileRepository.save(files);
        return savedFile;
    }

    public String getCloudUrl(String localUrl) {
         if (fileRepository.findByLocalUrl(localUrl) != null) {
             return fileRepository.findByLocalUrl(localUrl).getCloudUrl();
         } else {
             throw new RuntimeException("file local url dont have mapped cloud url");
         }
    }

    public void deleteFileByCloud(String cloutUrl) {
        fileRepository.deleteByCloudUrl(cloutUrl);
    }
}
