package com.foodflow.delivery_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryPartnerSummaryDto {

    private Long partnerId;
    private String name;
    private String phone;

    private String vehicleType;
}
