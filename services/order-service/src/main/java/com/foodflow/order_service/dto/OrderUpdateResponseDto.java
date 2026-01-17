package com.foodflow.order_service.dto;

import com.foodflow.order_service.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderUpdateResponseDto {
    private Long orderId;
    private OrderStatus status;
}
