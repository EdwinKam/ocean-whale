package com.ocean.whale.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.ocean.whale.exception.WhaleException;
import com.ocean.whale.exception.WhaleServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FirestoreService {

    private final Firestore firestore;

    @Autowired
    public FirestoreService(Firestore firestore) {
        this.firestore = firestore;
    }

    public String addDocument(String collectionName, String documentId, Map<String, Object> data) {
        try {
            CollectionReference collection = firestore.collection(collectionName);
            ApiFuture<WriteResult> future = collection.document(documentId).set(data);
            return future.get().getUpdateTime().toString();
        } catch (Exception e) {
            throw new WhaleServiceException(WhaleException.FIREBASE_ERROR, String.format("failed to set collectionName: %s documentId: %s", collectionName, documentId));
        }
    }

    public String addDocument(String collectionName, String documentId, Object data) {
        try {
            CollectionReference collection = firestore.collection(collectionName);
            ApiFuture<WriteResult> future = collection.document(documentId).set(data);
            return future.get().getUpdateTime().toString();
        } catch (Exception e) {
            throw new WhaleServiceException(WhaleException.FIREBASE_ERROR, String.format("failed to set collectionName: %s documentId: %s", collectionName, documentId));
        }
    }

    public Map<String, Object> getDocument(String collectionName, String documentId) {
        try {
            DocumentReference docRef = firestore.collection(collectionName).document(documentId);
            return docRef.get().get().getData();
        } catch (Exception e) {
            throw new WhaleServiceException(WhaleException.FIREBASE_ERROR, String.format("failed to get collectionName: %s documentId: %s", collectionName, documentId));
        }
    }

    public List<Map<String, Object>> getDocuments(String collectionName)
            throws ExecutionException, InterruptedException {
        CollectionReference docRef = firestore.collection(collectionName);
        Iterator<DocumentReference> iterator = docRef.listDocuments().iterator();
        List<Map<String, Object>> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next().get().get().getData());
        }

        return list;
    }

    public List<Map<String, Object>> getDocuments(String collectionName, Filter filter) {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection(collectionName).where(filter).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            return documents.stream().map(QueryDocumentSnapshot::getData).toList();
        } catch (Exception e) {
            throw new WhaleServiceException(WhaleException.FIREBASE_ERROR, String.format("failed to get collectionName: %s documentId: %s with filter %s", collectionName, filter));
        }
    }

    public boolean deleteDocument(String collectionName, String documentId)
            throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(collectionName).document(documentId);
        ApiFuture<WriteResult> writeResult = docRef.delete();
        // Optionally, you can check the result of the delete operation
        return writeResult.get() != null;
    }
}
