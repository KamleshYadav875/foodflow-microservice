package com.foodflow.restaurant_service.dto.owner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantOrdersDetailResponse {
    private Long orderId;
    private String status;
    private LocalTime placeAtTime;
    private LocalDate placeAtDate;
    private BigDecimal totalAmount;
    private List<OrderItemsDetail> orderItems;
    private OrderCustomerAddress address;
    private String customerPhone;
}
