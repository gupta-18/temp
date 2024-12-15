package com.example.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            imageService.saveImage(file);
            return ResponseEntity.status(HttpStatus.OK).body("Image uploaded successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        ImageEntity imageEntity = imageService.getImageById(id);
        if (imageEntity != null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, imageEntity.getType()) // Correct MIME type for the image
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + imageEntity.getName() + "\"") // For inline display
                    .body(imageEntity.getData()); // Image data as byte array
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
