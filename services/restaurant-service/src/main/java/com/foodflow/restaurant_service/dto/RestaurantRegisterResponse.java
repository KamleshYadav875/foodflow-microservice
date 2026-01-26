package com.foodflow.restaurant_service.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestaurantRegisterResponse {
    private boolean success;
    private String message;
}
