package com.foodflow.restaurant_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantRegisterRequest {
    private String restaurantName;
    private String description;
    private String address;
    private String city;
    private String pincode;
    private String ownerName;
    private String email;
    private String phone;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private String fssaiNumber;
    private String gstNumber;
    private String panNumber;
    private String bankAccountNumber;
    private String ifscCode;
}

