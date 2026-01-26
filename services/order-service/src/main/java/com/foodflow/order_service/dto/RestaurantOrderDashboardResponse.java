package com.foodflow.order_service.dto;

import com.foodflow.order_service.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantOrderDashboardResponse {
    private Long orderId;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private Integer items;
    private LocalDate placedAt;
}
