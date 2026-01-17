package com.foodflow.restaurant_service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RestaurantOwnerProfileResponseDto {

    // Core restaurant info
    private Long id;
    private String name;
    private String description;
    private Boolean isOpen;
    private Float rating;
    private String imageUrl;

    // Location
    private String address;
    private String city;
    private String state;
    private String zipcode;

    // Owner & status
    private Long ownerId;

    // Menu stats
    private Integer totalMenuItems;
    private Integer activeMenuItems;

    // Order stats (today or overall)
    private Long totalOrders;
    private Long pendingOrders;
    private Long completedOrders;

    // Metadata
    private LocalDateTime joinedAt;
}

