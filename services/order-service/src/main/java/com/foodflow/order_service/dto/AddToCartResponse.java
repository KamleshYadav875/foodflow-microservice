package com.foodflow.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartResponse {
    private Long cartId;
    private Long restaurantId;
    private Integer totalItems;
    private BigDecimal totalAmount;
}
