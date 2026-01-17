package com.foodflow.order_service.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemResponseDto {
    private Long id;
    private Long menuItemId;
    private String menuItemName;
    private BigDecimal totalPrice;
    private Integer quantity;
    private BigDecimal price;
}
