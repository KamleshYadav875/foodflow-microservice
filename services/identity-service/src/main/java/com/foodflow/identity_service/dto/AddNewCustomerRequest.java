package com.foodflow.identity_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddNewCustomerRequest {
    private Long userId;
    private String email;
    private String name;
    private String phone;
}
