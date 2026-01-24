package com.foodflow.customer_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCustomerProfileRequest {
    private String name;
    private String address;
    private String city;
    private String state;
    private String zipcode;

}
