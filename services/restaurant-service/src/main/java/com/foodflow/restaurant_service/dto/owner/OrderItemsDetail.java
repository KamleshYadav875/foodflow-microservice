package com.foodflow.restaurant_service.dto.owner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemsDetail {
    private Boolean isVeg;
    private String itemName;
    private Integer quantity;
}
