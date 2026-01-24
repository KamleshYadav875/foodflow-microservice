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
    private String address;
    private String city;
    private String state;
    private String zipcode;
    private Boolean isDefault;
    private String label;
}
