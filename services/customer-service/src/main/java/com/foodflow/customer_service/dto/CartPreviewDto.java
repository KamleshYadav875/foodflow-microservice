package com.foodflow.customer_service.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CartPreviewDto {

    private Boolean hasItems;
    private Integer itemCount;
    private BigDecimal totalAmount;
    private String restaurantName;
}
