package com.foodflow.restaurant_service.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MenuItemInternalDto {

    private Long id;
    private Long restaurantId;
    private String menuItemName;
    private Boolean isAvailable;
    private BigDecimal price;
}
