package com.foodflow.restaurant_service.service;


import com.foodflow.restaurant_service.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RestaurantService {

    RestaurantDetailResponseDto createRestaurant(RestaurantRequestDto request, MultipartFile image);

    List<RestaurantListResponseDto> getAllRestaurantsByCity(String city);

    RestaurantDetailResponseDto getRestaurantById(Long restaurantId);

    void changeRestaurantState(Long userId);

    void updateRestaurantImage(Long userId,  MultipartFile image);

    RestaurantDetailResponse getRestaurantDetailById(Long restaurantId);

    RestaurantMenuResponseDto getMenuByRestaurant(Long restaurantId);

    RestaurantOwnerProfileResponseDto getRestaurantProfile(Long userId);

    RestaurantOwnerProfileResponseDto updateRestaurantProfile(Long userId);
}
