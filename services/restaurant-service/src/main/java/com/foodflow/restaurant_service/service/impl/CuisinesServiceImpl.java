package com.foodflow.restaurant_service.service.impl;

import com.foodflow.restaurant_service.dto.CuisinesRequest;
import com.foodflow.restaurant_service.dto.CuisinesResponse;
import com.foodflow.restaurant_service.entity.Cuisines;
import com.foodflow.restaurant_service.entity.Restaurant;
import com.foodflow.restaurant_service.exceptions.ResourceNotFoundException;
import com.foodflow.restaurant_service.repository.CuisinesRepository;
import com.foodflow.restaurant_service.repository.RestaurantRepository;
import com.foodflow.restaurant_service.service.CuisinesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CuisinesServiceImpl implements CuisinesService {

    private final CuisinesRepository cuisinesRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public CuisinesResponse createCuisines(Long userId, CuisinesRequest request) {

        Restaurant restaurant = restaurantRepository.findByOwnerId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        Cuisines cuisines = Cuisines.builder()
                .name(request.getName())
                .icon(request.getIcon())
                .image(request.getImage())
                .restaurantId(restaurant.getId())
                .build();

        Cuisines savedCuisine = cuisinesRepository.save(cuisines);
        return CuisinesResponse.builder()
                .id(savedCuisine.getId())
                .name(savedCuisine.getName())
                .icon(savedCuisine.getIcon())
                .image(savedCuisine.getImage())
                .build();
    }

    @Override
    public CuisinesResponse updateCuisines(Long userId, Long cuisineId, CuisinesRequest request) {
        Restaurant restaurant = restaurantRepository.findByOwnerId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        Cuisines cuisines = cuisinesRepository.findByIdAndRestaurantId(cuisineId, restaurant.getId());

        cuisines.setName(request.getName());
        cuisines.setIcon(request.getIcon());
        cuisines.setImage(request.getImage());

        Cuisines savedCuisine = cuisinesRepository.save(cuisines);
        return CuisinesResponse.builder()
                .id(savedCuisine.getId())
                .name(savedCuisine.getName())
                .icon(savedCuisine.getIcon())
                .image(savedCuisine.getImage())
                .build();
    }

    @Override
    public List<CuisinesResponse> getCuisines(Long userId) {
        Restaurant restaurant = restaurantRepository.findByOwnerId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        List<Cuisines> cuisines = cuisinesRepository.findByRestaurantId(restaurant.getId());

        return cuisines.stream().map(
                cuisine ->  CuisinesResponse.builder()
                        .id(cuisine.getId())
                        .name(cuisine.getName())
                        .icon(cuisine.getIcon())
                        .image(cuisine.getImage())
                        .build()
        ).toList();
    }
}
