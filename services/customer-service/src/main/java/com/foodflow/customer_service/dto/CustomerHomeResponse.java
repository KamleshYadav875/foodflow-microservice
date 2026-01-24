package com.foodflow.customer_service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CustomerHomeResponse {

    private List<AddressResponse> address;
    private List<RestaurantCardDto> restaurants;
    private CartPreviewDto cartPreview;
}
