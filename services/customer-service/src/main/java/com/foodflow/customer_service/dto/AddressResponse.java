package com.foodflow.customer_service.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddressResponse {

    private Long id;
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
