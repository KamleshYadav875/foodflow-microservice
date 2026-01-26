package com.foodflow.restaurant_service.dto.owner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCustomerAddress {
    private String id;
    private String type;
    private String label;
    private String fullAddress;
    private String lat;
    private String lng;
}
