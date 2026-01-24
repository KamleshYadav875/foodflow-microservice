package com.foodflow.customer_service.dto;

import com.foodflow.customer_service.enums.UserRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class CustomerProfileResponseDto {

    private Long userId;
    private String name;
    private String phone;
    private String email;
    private String profileImageUrl;

    private Integer totalOrders;
    private Integer activeOrders;
    private Integer cancelledOrders;
    private List<AddressResponse> address;

    private LocalDateTime joinedAt;
}
