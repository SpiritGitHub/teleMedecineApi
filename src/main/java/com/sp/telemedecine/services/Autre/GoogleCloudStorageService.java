package com.sp.telemedecine.services.Autre;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class GoogleCloudStorageService {

    private final Storage storage;
    private final String bucketName;

    public GoogleCloudStorageService(@Value("${google.cloud.project-id}") String projectId,
                                     @Value("${google.cloud.bucket-name}") String bucketName,
                                     @Value("${google.application.credentials}") String credentialsPath) throws IOException {
        this.bucketName = bucketName;

        try (FileInputStream serviceAccountStream = new FileInputStream(credentialsPath)) {
            this.storage = StorageOptions.newBuilder()
                    .setProjectId(projectId)
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                    .build()
                    .getService();
        }
    }

    public String uploadFile(MultipartFile file, String role) throws IOException {
        String fileName = role + "_" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, fileName).build(),
                file.getBytes()
        );
        return blobInfo.getMediaLink();
    }
}
