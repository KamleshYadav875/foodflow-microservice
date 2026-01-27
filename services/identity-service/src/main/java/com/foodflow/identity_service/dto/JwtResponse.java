package com.foodflow.identity_service.dto;

import com.foodflow.identity_service.enums.UserRole;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private Set<UserRole> roles;
}
