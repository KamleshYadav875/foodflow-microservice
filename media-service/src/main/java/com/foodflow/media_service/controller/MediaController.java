package com.foodflow.media_service.controller;

import com.foodflow.media_service.exception.FileStorageException;
import com.foodflow.media_service.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/media")
public class MediaController {

    private final FileStorageService fileStorageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadMedia(@RequestPart(value = "image", required = false) MultipartFile image, @RequestPart(value = "folder") String folder){
        return ResponseEntity.ok(fileStorageService.upload(image, folder));
    }
}
