package com.foodflow.delivery_service.dto;

import com.foodflow.delivery_service.enums.VehicleType;
import lombok.Data;

@Data
public class RegisterDeliveryPartnerRequest {
    private String city;
    private VehicleType vehicleType;
}
