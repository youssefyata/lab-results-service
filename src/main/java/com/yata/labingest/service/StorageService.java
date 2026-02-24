package com.yata.labingest.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final AmazonS3 storageClient;

    public String uploadPdf(String bucketName, String objectName, InputStream inputStream, long contentLength) {
        boolean found = storageClient.doesBucketExistV2(bucketName);
        if (!found) {
            storageClient.createBucket(bucketName);
        }

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentLength);
        metadata.setContentType("application/pdf");
        metadata.setContentDisposition("inline; filename=\"" + objectName + "\"");

        storageClient.putObject(
                new PutObjectRequest(bucketName, objectName, inputStream, metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        return storageClient.getUrl(bucketName, objectName).toString();
    }
}
