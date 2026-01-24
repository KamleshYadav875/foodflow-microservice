package com.foodflow.customer_service.controller;

import com.foodflow.customer_service.dto.CustomerHomeResponse;
import com.foodflow.customer_service.dto.CustomerProfileResponseDto;
import com.foodflow.customer_service.dto.UpdateCustomerProfileRequest;
import com.foodflow.customer_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/me")
    public ResponseEntity<CustomerProfileResponseDto> getMyProfile(@RequestHeader("X-USER-ID") Long userId) {
        CustomerProfileResponseDto customerProfileResponseDto = customerService.getMyProfile(userId);
        return ResponseEntity.ok(customerProfileResponseDto);
    }

    @PutMapping("/me")
    public ResponseEntity<CustomerProfileResponseDto> updateProfile(@RequestBody UpdateCustomerProfileRequest request){
        CustomerProfileResponseDto response = customerService.updateProfile(request);
        return new ResponseEntity<CustomerProfileResponseDto>(response, HttpStatus.OK);
    }

    @PatchMapping(value = "/me/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateMenuItemImage(@RequestPart(value = "image", required = false) MultipartFile image){
        customerService.updateUserImage(image);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<CustomerHomeResponse> home(@RequestHeader("X-USER-ID") Long userId) {

        return ResponseEntity.ok(customerService.home(userId));
    }

}
