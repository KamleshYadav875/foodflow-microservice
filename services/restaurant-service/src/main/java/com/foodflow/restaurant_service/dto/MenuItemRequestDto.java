package com.foodflow.restaurant_service.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MenuItemRequestDto {
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private Boolean isVeg;
    private Boolean isAvailable;
}
