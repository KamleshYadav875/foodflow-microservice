package com.foodflow.api_gateway.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GatewayErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;
}
