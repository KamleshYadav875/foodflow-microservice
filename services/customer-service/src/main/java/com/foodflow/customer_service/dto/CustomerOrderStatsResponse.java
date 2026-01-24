package com.foodflow.customer_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrderStatsResponse {

    long totalOrders;
    long activeOrders;
    long cancelledOrders;

}