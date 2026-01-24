package com.foodflow.restaurant_service.service;

import com.foodflow.restaurant_service.dto.CuisinesRequest;
import com.foodflow.restaurant_service.dto.CuisinesResponse;

import java.util.List;

public interface CuisinesService {
    CuisinesResponse createCuisines(Long userId, CuisinesRequest request);

    CuisinesResponse updateCuisines(Long userId, Long cuisineId, CuisinesRequest request);

    List<CuisinesResponse> getCuisines(Long userId);
}
