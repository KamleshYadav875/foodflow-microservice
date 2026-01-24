package com.foodflow.customer_service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RestaurantSectionDto {

    private Integer total;
    private List<RestaurantCardDto> items;
}
