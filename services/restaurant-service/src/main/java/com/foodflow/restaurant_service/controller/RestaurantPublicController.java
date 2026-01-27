package com.foodflow.restaurant_service.controller;


import com.foodflow.restaurant_service.dto.*;
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
@RequestMapping("/api/restaurants")
public class RestaurantPublicController {

    private final RestaurantService restaurantService;

    // Create restaurant
    @PostMapping(value = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantRegisterResponse> registerNewRestaurant(@RequestBody RestaurantRegisterRequest request){
        RestaurantRegisterResponse response = restaurantService.registerNewRestaurant(request);
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
