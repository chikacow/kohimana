package com.chikacow.kohimana.service;

import com.chikacow.kohimana.exception.FileUploadException;
import io.minio.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class MinioService {

    @Value("${minio.bucket}")
    private String bucket;

    private final MinioClient minioClient;

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public String uploadFile(MultipartFile file) {

        try {

            String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();

            // Đảm bảo bucket tồn tại
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }

            // Upload file
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(filename)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());

            // Trả về link (nếu bucket public thì bạn có thể mở được ngay)
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucket)
                            .object(filename)
                            .method(Method.GET)
                            .expiry(60 * 60) // URL sống 1 giờ
                            .build()
            );
        } catch (Exception e) {
            throw new FileUploadException("problem with file upload, maybe not uploaded");
        }
    }
}

