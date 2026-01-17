package com.foodflow.delivery_service.dto;

import com.foodflow.delivery_service.enums.DeliveryPartnerAvailability;
import lombok.Data;

@Data
public class UpdateAvailabilityRequest {

    private DeliveryPartnerAvailability availability;
}
