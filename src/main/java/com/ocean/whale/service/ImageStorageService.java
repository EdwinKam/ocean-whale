package com.ocean.whale.service;

import com.ocean.whale.model.Post;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageStorageService {
    public void uploadImage(MultipartFile image, String fileName) {
        try {
            Path directoryPath = Paths.get("cache/images");
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            // Save the image to the local filesystem
            if (fileName != null && !fileName.isEmpty()) {
                Path filePath = directoryPath.resolve(fileName);
                Files.write(filePath, image.getBytes());

                // Print the path where the image is saved
                System.out.println("Saved image to: " + filePath.toAbsolutePath().toString());
            } else {
                System.err.println("Invalid file name.");
            }

        } catch (IOException e) {
            System.err.println("Failed to save image: " + (image != null ? image.getOriginalFilename() : "unknown"));
            e.printStackTrace();
        }
    }
}
