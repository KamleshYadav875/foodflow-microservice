package com.foodflow.customer_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InternalCustomerResponse {

    private Long id;
    private String name;
}
