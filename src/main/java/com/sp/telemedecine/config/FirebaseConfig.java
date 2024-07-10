package com.sp.telemedecine.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.Bucket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.config.path}")
    private String firebaseConfigPath;

    @Value("${firebase.database.url}")
    private String firebaseDatabaseUrl;

    @Value("${firebase.storage.bucket}")
    private String firebaseStorageBucket;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        FileInputStream serviceAccount = new FileInputStream(firebaseConfigPath);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(firebaseDatabaseUrl)
                .setStorageBucket(firebaseStorageBucket)
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public Storage getStorage() {
        return StorageOptions.getDefaultInstance().getService();
    }

    @Bean
    public Bucket getBucket() {
        return getStorage().get(firebaseStorageBucket);
    }
}
