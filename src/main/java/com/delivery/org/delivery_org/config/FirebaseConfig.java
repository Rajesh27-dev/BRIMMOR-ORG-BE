package com.delivery.orgservice.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initFirebase() {
        try {
            if (!FirebaseApp.getApps().isEmpty()) return;

            String path = System.getenv("FIREBASE_SERVICE_ACCOUNT_PATH");
            if (path == null) {
                throw new IllegalStateException("FIREBASE_SERVICE_ACCOUNT_PATH not set");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(
                            GoogleCredentials.fromStream(new FileInputStream(path))
                    )
                    .build();

            FirebaseApp.initializeApp(options);
            System.out.println("✅ Firebase initialized successfully");

        } catch (Exception e) {
            e.printStackTrace(); // IMPORTANT
            throw new RuntimeException("❌ Firebase init failed", e);
        }
    }
}
