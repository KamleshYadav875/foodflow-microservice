package com.foodflow.order_service.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemResponse {

    private String name;

    private Integer quantity;

    private BigDecimal price;
    private BigDecimal totalAmount;
}
