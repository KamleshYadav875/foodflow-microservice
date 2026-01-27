package com.foodflow.customer_service.client;

import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface MediaServiceClient {

    @PostExchange(
            value = "/api/media/upload",
            contentType  = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    String uploadImage(
            @RequestPart("image") org.springframework.core.io.Resource image,
            @RequestPart("folder") String folder
    );
}
