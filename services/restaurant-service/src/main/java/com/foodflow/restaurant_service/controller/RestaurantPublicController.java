package com.foodflow.restaurant_service.controller;


import com.foodflow.restaurant_service.dto.RestaurantDetailResponseDto;
import com.foodflow.restaurant_service.dto.RestaurantListResponseDto;
import com.foodflow.restaurant_service.dto.RestaurantMenuResponseDto;
import com.foodflow.restaurant_service.dto.RestaurantRequestDto;
import com.foodflow.restaurant_service.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public/restaurants")
public class RestaurantPublicController {

    private final RestaurantService restaurantService;

    // Create restaurant
    @PostMapping(value = "/onboardrestaurant", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestaurantDetailResponseDto> createRestaurant(@RequestPart("restaurant") RestaurantRequestDto request, @RequestPart(value = "image", required = false) MultipartFile image){
        RestaurantDetailResponseDto response = restaurantService.createRestaurant(request, image);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Get restaurant menu
    @GetMapping("/{restaurantId}/menu")
    public RestaurantMenuResponseDto getMenu(@PathVariable Long restaurantId) {
        return restaurantService.getMenuByRestaurant(restaurantId);
    }

    // Get restaurants by city / location
    @GetMapping()
    public ResponseEntity<List<RestaurantListResponseDto>> getAllRestaurantsByCity(@RequestParam("city") String city){
        List<RestaurantListResponseDto> response = restaurantService.getAllRestaurantsByCity(city);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Get restaurant details
    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantDetailResponseDto> getRestaurantDetails(@PathVariable Long restaurantId){
        RestaurantDetailResponseDto response = restaurantService.getRestaurantById(restaurantId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
