package com.SoT.JIN.story;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Service
public class S3Service {
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;
    private final S3Presigner s3Presigner;

    String createPresignedUrl(String path) {
        PutObjectRequest putObjectRequest = (PutObjectRequest)PutObjectRequest.builder().bucket(this.bucket).key(path).build();
        PutObjectPresignRequest preSignRequest = PutObjectPresignRequest.builder().signatureDuration(Duration.ofMinutes(3L)).putObjectRequest(putObjectRequest).build();
        return this.s3Presigner.presignPutObject(preSignRequest).url().toString();
    }

    public S3Service(final S3Presigner s3Presigner) {
        this.s3Presigner = s3Presigner;
    }
}
