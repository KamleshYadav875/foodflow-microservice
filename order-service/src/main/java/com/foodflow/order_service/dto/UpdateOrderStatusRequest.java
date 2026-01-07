package com.foodflow.order_service.dto;

import com.foodflow.order_service.enums.CancelReason;
import com.foodflow.order_service.enums.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    private OrderStatus status;
    private CancelReason cancelReason;
}
