package com.foodflow.restaurant_service.controller;

import com.foodflow.restaurant_service.dto.RestaurantOwnerProfileResponseDto;
import com.foodflow.restaurant_service.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurant")
public class RestaurantOwnerController {

    private final RestaurantService restaurantService;

    // Open / close restaurant
    @PutMapping("/status")
    public ResponseEntity<Void> changeRestaurantState(@RequestHeader("X-USER-ID") Long userId){
        restaurantService.changeRestaurantState(userId);
        return  ResponseEntity.noContent().build();
    }

    // Update restaurant image
    @PutMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateRestaurantImage(@RequestHeader("X-USER-ID") Long userId ,  @RequestPart(value = "image", required = false) MultipartFile image){
        restaurantService.updateRestaurantImage(userId, image);
        return ResponseEntity.noContent().build();
    }

    // Update restaurant profile - /api/restaurant/me
    @PutMapping(value = "/me")
    public ResponseEntity<RestaurantOwnerProfileResponseDto> updateRestaurantProfile(@RequestHeader("X-USER-ID") Long userId){
        return ResponseEntity.ok(restaurantService.updateRestaurantProfile(userId));
    }

    // Get Restaurant info
    @GetMapping("/me")
    public ResponseEntity<RestaurantOwnerProfileResponseDto> getRestaurantProfile(@RequestHeader("X-USER-ID") Long userId){
        return ResponseEntity.ok(restaurantService.getRestaurantProfile(userId));
    }

}
