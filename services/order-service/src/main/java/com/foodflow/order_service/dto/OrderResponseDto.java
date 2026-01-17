package com.foodflow.order_service.dto;

import com.foodflow.order_service.enums.CancelReason;
import com.foodflow.order_service.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderResponseDto {
    private Long orderId;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private Integer totalItems;
    private LocalDateTime createdAt;
    private String paymentLink;
    private String paymentId;
    private LocalDateTime cancelledAt;
    private CancelReason cancelReason;
}
