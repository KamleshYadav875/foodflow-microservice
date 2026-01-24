package com.foodflow.restaurant_service.controller;

import com.foodflow.restaurant_service.dto.CuisinesRequest;
import com.foodflow.restaurant_service.dto.CuisinesResponse;
import com.foodflow.restaurant_service.dto.OfferRequest;
import com.foodflow.restaurant_service.dto.OfferResponse;
import com.foodflow.restaurant_service.service.CuisinesService;
import com.foodflow.restaurant_service.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant/cuisines")
@RequiredArgsConstructor
public class CuisinesController {

    private final CuisinesService cuisinesService;

    @PostMapping
    public ResponseEntity<CuisinesResponse> createCuisines(@RequestHeader("X-USER-ID") Long userId, @RequestBody CuisinesRequest request){
        return new ResponseEntity<CuisinesResponse>(cuisinesService.createCuisines(userId, request), HttpStatus.CREATED);
    }

    @PutMapping("/{cuisineId}")
    public ResponseEntity<CuisinesResponse> updateCuisines(@RequestHeader("X-USER-ID") Long userId, @PathVariable Long cuisineId,  @RequestBody CuisinesRequest request){
        return new ResponseEntity<CuisinesResponse>(cuisinesService.updateCuisines(userId, cuisineId, request), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CuisinesResponse>> getCuisines(@RequestHeader("X-USER-ID") Long userId){
        return new ResponseEntity<List<CuisinesResponse>>(cuisinesService.getCuisines(userId), HttpStatus.OK);
    }
}
