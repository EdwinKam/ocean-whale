package com.ocean.whale.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

@Configuration
public class S3ClientConfig {
    public static Region AWS_REGION = Region.US_EAST_2;
    @Value("${aws.whale.access.key}")
    private String accessKey;
    @Value("${aws.whale.secret.access.key}")
    private String secretAccessKey;

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                accessKey,
                secretAccessKey
        );

        return S3Client.builder()
                .region(AWS_REGION)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }
}
