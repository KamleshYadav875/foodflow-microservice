package com.foodflow.customer_service.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MessageResponse {

    private String message;
}
