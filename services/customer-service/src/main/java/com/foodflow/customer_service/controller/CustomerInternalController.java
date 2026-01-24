package com.foodflow.customer_service.controller;

import com.foodflow.customer_service.dto.AddNewCustomerRequest;
import com.foodflow.customer_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/customer")
@RequiredArgsConstructor
public class CustomerInternalController {

    private final CustomerService customerService;

    @PostMapping("/add")
    public ResponseEntity<Void> addCustomer(@RequestBody AddNewCustomerRequest request){
        customerService.addNewCustomer(request);
        return ResponseEntity.noContent().build();
    }
}
