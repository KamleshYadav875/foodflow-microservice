package com.foodflow.order_service.dto;

import com.foodflow.order_service.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderDetailResponse {

    private Long orderId;
    private OrderStatus status;
    private LocalDateTime orderedAt;
    private String restaurantName;
    private Long partnerId;
    private String partnerName;
    private Double partnerRating;
    private List<OrderItemResponse> items;
}
