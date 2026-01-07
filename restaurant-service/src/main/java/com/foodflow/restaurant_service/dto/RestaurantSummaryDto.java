package com.foodflow.restaurant_service.dto;

import lombok.Data;

@Data
public class RestaurantSummaryDto {
    private Long id;
    private String name;
    private String city;
    private Float rating;
}
