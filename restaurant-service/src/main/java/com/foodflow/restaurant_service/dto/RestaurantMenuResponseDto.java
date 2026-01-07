package com.foodflow.restaurant_service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RestaurantMenuResponseDto {
    private RestaurantSummaryDto restaurant;
    private List<MenuCategoryResponseDto> categories;
}
