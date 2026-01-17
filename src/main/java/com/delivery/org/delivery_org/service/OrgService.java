package com.delivery.orgservice.service;

import com.delivery.orgservice.utils.OrgIdGenerator;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrgService {

    private static final String COLLECTION = "organizations";

    public Map<String, Object> createOrg(String name) throws Exception {

        Firestore db = FirestoreClient.getFirestore();

        String orgId = OrgIdGenerator.generate();

        Map<String, Object> org = new HashMap<>();
        org.put("orgId", orgId);
        org.put("name", name);
        org.put("status", "ACTIVE");
        org.put("createdAt", Instant.now().toString());

        db.collection(COLLECTION).document(orgId).set(org);

        return org;
    }

    public List<Map<String, Object>> getAllOrgs() throws Exception {

        Firestore db = FirestoreClient.getFirestore();

        return db.collection(COLLECTION)
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(doc -> doc.getData())
                .collect(Collectors.toList());
    }
    public Map<String, Object> getOrgById(String orgId) throws Exception {

    Firestore db = FirestoreClient.getFirestore();

    var docSnapshot = db.collection(COLLECTION)
            .document(orgId)
            .get()
            .get();

    if (!docSnapshot.exists()) {
        throw new RuntimeException("Organization not found: " + orgId);
    }

    return docSnapshot.getData();
}

}
