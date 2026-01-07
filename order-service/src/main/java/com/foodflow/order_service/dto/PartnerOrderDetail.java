package com.foodflow.order_service.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PartnerOrderDetail {

    // Order info
    private Long orderId;
    private String orderStatus;
    private BigDecimal totalAmount;
    private Integer totalItems;
    private LocalDateTime orderedAt;

    // Restaurant (pickup)
    private String restaurantName;
    private String restaurantAddress;
    private Double restaurantLatitude;
    private Double restaurantLongitude;

    // Customer (drop)
    private String customerName;
    private String deliveryAddress;
    private Double deliveryLatitude;
    private Double deliveryLongitude;

}
