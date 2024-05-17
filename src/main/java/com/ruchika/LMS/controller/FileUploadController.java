package com.ruchika.LMS.controller;

import com.ruchika.LMS.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/files")
public class FileUploadController {

    private final S3Service s3Service;

    @Autowired
    public FileUploadController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String response = s3Service.uploadFile(file);
            // Return the response from the S3Service with a 200 status code and CORS
            // headers set to *
            return ResponseEntity.ok().header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "*").header("Access-Control-Allow-Headers", "*")
                    .body(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }
}
