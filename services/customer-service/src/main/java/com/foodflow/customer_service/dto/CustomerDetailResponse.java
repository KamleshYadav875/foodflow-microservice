package com.foodflow.customer_service.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class CustomerDetailResponse {

    private Long id;
    private String customerName;
    private String customerPhone;
    private String deliveryAddress;
    private Double deliveryLatitude;
    private Double deliveryLongitude;
}
