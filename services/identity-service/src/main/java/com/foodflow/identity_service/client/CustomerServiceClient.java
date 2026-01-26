package com.foodflow.identity_service.client;

import com.foodflow.identity_service.dto.AddNewCustomerRequest;
import com.foodflow.identity_service.dto.UserOrderStatsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "CUSTOMER-SERVICE")
public interface CustomerServiceClient {

    @PostMapping("/internal/customer/add")
    Void addCustomer(@RequestBody AddNewCustomerRequest request);

}
