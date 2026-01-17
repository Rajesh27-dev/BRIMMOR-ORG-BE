package com.delivery.orgservice.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initFirebase() {
        try {
            if (!FirebaseApp.getApps().isEmpty()) return;

            byte[] credentialBytes = resolveCredentials();

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(
                            GoogleCredentials.fromStream(
                                    new ByteArrayInputStream(credentialBytes)
                            )
                    )
                    .build();

            FirebaseApp.initializeApp(options);
            System.out.println("✅ Firebase initialized successfully");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Firebase init failed", e);
        }
    }

    private byte[] resolveCredentials() {

        // 1️⃣ Preferred: BASE64 (Railway-safe, most reliable)
        String base64 = System.getenv("FIREBASE_SERVICE_ACCOUNT_BASE64");
        if (base64 != null && !base64.isBlank()) {
            return Base64.getDecoder().decode(base64);
        }

        // 2️⃣ Fallback: RAW JSON (must be valid JSON string)
        String rawJson = System.getenv("FIREBASE_SERVICE_ACCOUNT_JSON");
        if (rawJson != null && !rawJson.isBlank()) {
            return rawJson.getBytes(StandardCharsets.UTF_8);
        }

        throw new IllegalStateException(
                "Firebase credentials not found. Set FIREBASE_SERVICE_ACCOUNT_BASE64 or FIREBASE_SERVICE_ACCOUNT_JSON"
        );
    }
}
