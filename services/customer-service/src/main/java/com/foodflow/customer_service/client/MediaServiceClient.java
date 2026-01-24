package com.foodflow.customer_service.client;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface MediaServiceClient {

    @PostMapping(
            value = "/api/media/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    String uploadImage(
            @RequestPart("image") MultipartFile image,
            @RequestPart("folder") String folder
    );
}
