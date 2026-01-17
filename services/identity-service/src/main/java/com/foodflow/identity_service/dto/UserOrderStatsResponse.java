package com.foodflow.identity_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOrderStatsResponse {

    long totalOrders;
    long activeOrders;
    long cancelledOrders;

}