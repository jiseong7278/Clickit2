package com.project.clickit.controller;

import com.project.clickit.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RequestMapping(value = "${requestMapping.imageUpload}", produces="application/json;charset=UTF-8")
public class ImageUploadController {

    private final ImageUploadService imageUploadService;

    @PostMapping("${imageUpload.upload}")
    public ResponseEntity<String> upload(@RequestPart(value = "image") MultipartFile image) {
        return ResponseEntity.ok().body(imageUploadService.upload(image));
    }

    @GetMapping("${imageUpload.delete}")
    public ResponseEntity<Void> delete(@RequestParam String imageUrl) {
        imageUploadService.deleteImageFromS3(imageUrl);
        return ResponseEntity.ok().build();
    }
}
