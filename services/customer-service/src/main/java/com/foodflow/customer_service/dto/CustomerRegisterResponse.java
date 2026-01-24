package com.foodflow.customer_service.dto;

import com.foodflow.customer_service.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CustomerRegisterResponse {
    private Long id;
    private String name;
    private String phone;
    private Set<UserRole> roles;
    private String profileImageUrl;
}
