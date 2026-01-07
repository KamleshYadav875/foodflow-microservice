package com.foodflow.order_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserOrderStatsResponse {

    long totalOrders;
    long activeOrders;
    long cancelledOrders;

}
