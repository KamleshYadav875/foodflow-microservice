package com.foodflow.order_service.dto;

import com.foodflow.order_service.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderCheckoutResponseDto {
    private Long orderId;
    private String paymentId;
    private String paymentLink;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private Integer totalItems;
    private LocalDateTime createdAt;
}
