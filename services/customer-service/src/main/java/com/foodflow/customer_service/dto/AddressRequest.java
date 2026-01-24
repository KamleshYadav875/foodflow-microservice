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

    private Long id;

    private String address;

    private String city;

    private String state;

    private String zipcode;

    private Boolean isDefault;

    private String label;
}
