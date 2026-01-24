package com.foodflow.restaurant_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CuisinesResponse {
    private Long id;
    private String name;
    private String icon;
    private String image;
}
