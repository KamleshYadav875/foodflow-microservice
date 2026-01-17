package com.foodflow.restaurant_service.controller;

import com.foodflow.restaurant_service.dto.RestaurantDetailResponse;
import com.foodflow.restaurant_service.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurant/internal")
public class RestaurantInternalController {

    private final RestaurantService restaurantService;

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantDetailResponse> getRestaurantDetailById(@PathVariable Long restaurantId){
        return ResponseEntity.ok(restaurantService.getRestaurantDetailById(restaurantId));
    }
}
