package com.ocean.whale.service;

import com.ocean.whale.exception.WhaleException;
import com.ocean.whale.exception.WhaleServiceException;
import com.ocean.whale.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.IOException;

import static com.ocean.whale.configuration.S3ClientConfig.AWS_REGION;

@Service
public class ImageStorageService {
    private final S3Client s3Client;
    private final String bucketName; // Add this field to store the bucket name

    @Autowired
    public ImageStorageService(S3Client s3Client) {
        this.s3Client = s3Client;
        this.bucketName = "ocean-app-post-images"; // Initialize with your actual bucket name
    }

    public String uploadImage(MultipartFile image, String fileName) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(image.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(image.getBytes()));
            return constructS3Url(bucketName, AWS_REGION.toString(), fileName);
        } catch (Exception e) {
            throw new WhaleServiceException(WhaleException.S3_UPLOAD_ERROR, "error uploading image");
        }
    }

    private String constructS3Url(String bucketName, String region, String key) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
    }
}