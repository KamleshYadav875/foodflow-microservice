package com.foodflow.restaurant_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInternalResponseDto {

    private Long id;
    private String name;
}
