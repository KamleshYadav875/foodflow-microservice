package com.foodflow.identity_service.dto;

import com.foodflow.identity_service.enums.UserRole;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class JwtResponse {
    private String token;
    private Set<UserRole> roles;
}
