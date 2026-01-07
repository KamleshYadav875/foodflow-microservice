package com.foodflow.order_service.dto;

import lombok.Data;

@Data
public class AddToCartRequest {
    private Long restaurantId;
    private Long menuItemId;
    private Integer quantity;
}
