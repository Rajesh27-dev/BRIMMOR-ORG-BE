package com.delivery.orgservice.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initFirebase() {
        try {
            // Prevent multiple initializations
            if (!FirebaseApp.getApps().isEmpty()) {
                return;
            }

            String projectId = System.getenv("FIREBASE_PROJECT_ID");
            String privateKey = System.getenv("FIREBASE_PRIVATE_KEY");

            if (projectId == null || privateKey == null) {
                throw new IllegalStateException(
                        "Firebase environment variables are missing"
                );
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setProjectId(projectId)
                    .setCredentials(
                            GoogleCredentials.fromStream(
                                    new ByteArrayInputStream(
                                            privateKey.replace("\\n", "\n")
                                                    .getBytes()
                                    )
                            )
                    )
                    .build();

            FirebaseApp.initializeApp(options);

            System.out.println("✅ Firebase initialized successfully");

        } catch (Exception e) {
            System.err.println("❌ Firebase initialization failed");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
