package com.ocean.whale.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocean.whale.repository.FirestoreService;

@RestController
@RequestMapping("/api")
public class HelloController {
    private final FirestoreService firestoreService;

    @Autowired
    public HelloController(FirestoreService firestoreService) {
        this.firestoreService = firestoreService;
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, World!";
    }

    @GetMapping("/testdb")
    public String testDatabaseOperations() {
        String collectionName = "testCollection";
        String documentId = "testDocument";
        Map<String, Object> data = new HashMap<>();
        data.put("key", "value");

        // Add a document
        String addResult = firestoreService.addDocument(collectionName, documentId, data);

        // Get the document
        Map<String, Object> retrievedData = firestoreService.getDocument(collectionName, documentId);
        if (retrievedData == null || !retrievedData.equals(data)) {
            return "Failure: Document retrieval mismatch.";
        }

        // Delete the document
        boolean deleteResult = firestoreService.deleteDocument(collectionName, documentId);
        if (!deleteResult) {
            return "Failure: Document deletion failed.";
        }

        return "Success: Document added, retrieved, and deleted successfully.";
    }
}
