package com.foodflow.customer_service.controller;

import com.foodflow.customer_service.dto.CustomerHomeResponse;
import com.foodflow.customer_service.dto.CustomerProfileResponseDto;
import com.foodflow.customer_service.dto.MessageResponse;
import com.foodflow.customer_service.dto.UpdateCustomerProfileRequest;
import com.foodflow.customer_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

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
    public ResponseEntity<MessageResponse> updateProfile(@RequestHeader("X-USER-ID") Long userId, @RequestBody UpdateCustomerProfileRequest request){
        customerService.updateProfile(userId, request);
        return new ResponseEntity<>(MessageResponse.builder().message("Your profile has been updated successfully.").build(), HttpStatus.OK);
    }

    @PatchMapping(value = "/me/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> updateMenuItemImage(@RequestHeader("X-USER-ID") Long userId, @RequestPart(value = "image", required = false) MultipartFile image){
        String imageUrl = customerService.updateUserImage(userId, image);
        return ResponseEntity.ok(Map.of("profileImageUrl", imageUrl));
    }

    @GetMapping
    public ResponseEntity<CustomerHomeResponse> home(@RequestHeader("X-USER-ID") Long userId) {

        return ResponseEntity.ok(customerService.home(userId));
    }

}
