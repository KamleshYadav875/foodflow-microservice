package com.foodflow.order_service.dto;

import com.foodflow.order_service.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class MyOrderResponseDto {

    private Long orderId;
    private OrderStatus status;

    private BigDecimal totalAmount;
    private Integer totalItems;

    private String restaurantName;

    private LocalDateTime createdAt;
}
