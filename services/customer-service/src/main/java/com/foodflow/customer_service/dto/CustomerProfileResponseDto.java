package com.foodflow.customer_service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CustomerProfileResponseDto {

    private Long userId;
    private String name;
    private String phone;
    private String email;
    private String profileImageUrl;

    private List<AddressResponse> addresses;
}
