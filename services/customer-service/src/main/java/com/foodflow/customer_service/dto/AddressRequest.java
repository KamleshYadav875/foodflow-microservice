package com.foodflow.customer_service.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class AddressRequest {

    private String type;
    private String address;
    private String city;
    private String state;
    private String zipcode;
    private String label;
    private String landmark;
    private Boolean isDefault;
    private Double lng;
    private Double lat;
}
