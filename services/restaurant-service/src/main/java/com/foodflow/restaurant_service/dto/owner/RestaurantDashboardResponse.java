package com.foodflow.restaurant_service.dto.owner;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestaurantDashboardResponse {
    private boolean isOpen;
    private Integer todayOrders;
    private Integer pendingOrders;
    private Integer preparingOrders;
    private BigDecimal todayRevenue;
    private Double averageRating;
    private Integer avgPrepTime;
    private List<RestaurantOrderDashboardResponse> newOrders;
    private List<RestaurantOrderDashboardResponse> preparingOrdersList;
    private List<RestaurantOrderDashboardResponse> readyOrders;
    private List<RestaurantOrderDashboardResponse> recentOrders;
}
