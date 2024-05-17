package com.ruchika.LMS.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName;

    public S3Service(
            @Value("${aws.accessKeyId}") String accessKeyId,
            @Value("${aws.secretKey}") String secretKey,
            @Value("${aws.region}") String region,
            @Value("${aws.s3.bucketName}") String bucketName) {

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretKey);
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
        this.bucketName = bucketName;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .putObjectRequest(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build())
                .build();
        
        S3Presigner presigner = S3Presigner.create();
        String presignedUrl = presigner.presignPutObject(presignRequest).url().toString();
        System.out.println("Presigned URL: " + presignedUrl);

        s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build(),
                software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

        return "File uploaded successfully: " + fileName;
    }
}
