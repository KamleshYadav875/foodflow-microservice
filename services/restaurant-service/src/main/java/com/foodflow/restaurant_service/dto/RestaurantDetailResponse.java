package com.foodflow.restaurant_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantDetailResponse {
    private Long restaurantId;
    private String restaurantName;
    private String restaurantAddress;
    private Double restaurantLatitude;
    private Double restaurantLongitude;
}
