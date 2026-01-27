package com.foodflow.identity_service.dto;

import com.foodflow.identity_service.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserRegisterResponse {
    private Long id;
    private String phone;
    private Set<UserRole> roles;
}
