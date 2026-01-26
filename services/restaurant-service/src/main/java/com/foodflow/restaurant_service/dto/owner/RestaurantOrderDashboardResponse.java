package com.foodflow.restaurant_service.dto.owner;

import com.foodflow.restaurant_service.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantOrderDashboardResponse {
    private Long orderId;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private Integer items;
    private LocalDate placedAt;
}
