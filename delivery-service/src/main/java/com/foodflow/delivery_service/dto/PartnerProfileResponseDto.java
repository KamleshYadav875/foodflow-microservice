package com.foodflow.delivery_service.dto;

import com.foodflow.delivery_service.enums.DeliveryPartnerAvailability;
import com.foodflow.delivery_service.enums.VehicleType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PartnerProfileResponseDto {

    private Long partnerId;
    private String name;
    private String city;
    private DeliveryPartnerAvailability availability;
    private VehicleType vehicleType;
    private Long totalDeliveries;
    private Double rating;
    private Boolean isActive;
    private LocalDateTime joinedAt;
}
