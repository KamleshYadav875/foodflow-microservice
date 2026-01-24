package com.foodflow.customer_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CuisineDto {

    private String id;
    private String name;
    private String imageUrl;
}