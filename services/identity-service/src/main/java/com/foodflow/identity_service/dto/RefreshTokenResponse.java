package com.foodflow.identity_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshTokenResponse {
    private String refreshToken;
    private String accessToken;
}
