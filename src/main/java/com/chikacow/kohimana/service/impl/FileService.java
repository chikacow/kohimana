package com.chikacow.kohimana.service.impl;

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

    public Long cleanAllFiles() {
        Long numFiles = fileRepository.count();
        fileRepository.deleteAll();
        return numFiles;
    }

    /**
     * handle the file upload
     * @param localUrl
     * @return
     */
    public String resolveImageUrl(String localUrl) {
        String rt = "not found";
        try {
            if (localUrl != null) {
                return getCloudUrl(localUrl);
            } else {
                return "no image";
            }
        } catch (Exception e) {
            log.info("Failed to resolve image url");
        }

        return rt;
    }
}
