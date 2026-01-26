package com.foodflow.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartPreviewDto {

    private Boolean hasItems;
    private Integer itemCount;
    private BigDecimal totalAmount;
    private String restaurantName;
}
