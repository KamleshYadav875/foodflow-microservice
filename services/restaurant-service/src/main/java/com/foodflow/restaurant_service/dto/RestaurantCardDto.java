package com.foodflow.restaurant_service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RestaurantCardDto {

    private Long id;
    private String name;
    private String description;
    private Boolean isOpen;
    private Float rating;
    private String imageUrl;

    private List<String> cuisine;

    private String deliveryTime;   // "30 mins"
    private String distance;       // "2.1 km"
    private Integer priceRange;

    private List<String> offers;
}
