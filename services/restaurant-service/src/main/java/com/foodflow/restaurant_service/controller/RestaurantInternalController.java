package com.foodflow.restaurant_service.controller;

import com.foodflow.restaurant_service.dto.RestaurantCardDto;
import com.foodflow.restaurant_service.dto.RestaurantDetailResponse;
import com.foodflow.restaurant_service.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurant/internal")
public class RestaurantInternalController {

    private final RestaurantService restaurantService;

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantDetailResponse> getRestaurantDetailById(@PathVariable Long restaurantId){
        return ResponseEntity.ok(restaurantService.getRestaurantDetailById(restaurantId));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantCardDto>> getRestaurants(@RequestParam String city){
        return ResponseEntity.ok(restaurantService.getRestaurants(city));
    }
}
