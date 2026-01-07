package com.foodflow.restaurant_service.service.impl;


import com.foodflow.restaurant_service.client.MediaServiceClient;
import com.foodflow.restaurant_service.client.UserServiceClient;
import com.foodflow.restaurant_service.dto.*;
import com.foodflow.restaurant_service.entity.MenuItems;
import com.foodflow.restaurant_service.entity.Restaurant;
import com.foodflow.restaurant_service.exceptions.BadRequestException;
import com.foodflow.restaurant_service.exceptions.ResourceNotFoundException;
import com.foodflow.restaurant_service.repository.MenuItemRepository;
import com.foodflow.restaurant_service.repository.RestaurantRepository;
import com.foodflow.restaurant_service.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MediaServiceClient mediaServiceClient;
    private final UserServiceClient userServiceClient;
    private final MenuItemRepository menuItemRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public RestaurantDetailResponseDto createRestaurant(RestaurantRequestDto request, MultipartFile image) {

        UserInternalRequestDto userRequest = UserInternalRequestDto.builder()
                .phone(request.getPhone())
                .name(request.getName())
                .build();
        UserInternalResponseDto user = userServiceClient.onboardRestaurantAdmin(userRequest);

        if(restaurantRepository.existsByOwnerId(user.getId())){
            throw new BadRequestException("Restaurant is already registered with same phone");
        }
        String url = image == null ? null : mediaServiceClient.uploadImage(image, "restaurant");

        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .description(request.getDescription())
                .ownerId(user.getId())
                .rating(0f)
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .zipcode(request.getZipcode())
                .isOpen(true)
                .imageUrl(url)
                .build();

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return modelMapper.map(savedRestaurant, RestaurantDetailResponseDto.class);
    }

    @Override
    public List<RestaurantListResponseDto> getAllRestaurantsByCity(String city) {
        List<Restaurant> restaurants = restaurantRepository.findByCity(city);
        return restaurants.stream().filter(Restaurant::getIsOpen).map(r -> modelMapper.map(r, RestaurantListResponseDto.class)).toList();
    }

    @Override
    public RestaurantDetailResponseDto getRestaurantById(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        return modelMapper.map(restaurant, RestaurantDetailResponseDto.class);
    }

    @Override
    public void changeRestaurantState(Long userId) {
        Restaurant restaurant =  getRestaurantForOwner(userId);

        restaurant.setIsOpen(!restaurant.getIsOpen());
        restaurantRepository.save(restaurant);

    }

    @Override
    public void updateRestaurantImage(Long userId,  MultipartFile image) {

        if (image == null || image.isEmpty()) {
            throw new BadRequestException("Image file is required");
        }

        Restaurant restaurant =  getRestaurantForOwner(userId);

        String newImageUrl = mediaServiceClient.uploadImage(image, "restaurant");
        restaurant.setImageUrl(newImageUrl);

        restaurantRepository.save(restaurant);
    }

    @Override
    public RestaurantDetailResponse getRestaurantDetailById(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        return RestaurantDetailResponse.builder()
                .restaurantId(restaurant.getId())
                .restaurantName(restaurant.getName())
                .restaurantAddress(restaurant.getAddress())
                .restaurantLongitude(0.0)
                .restaurantLatitude(0.0)
                .build();
    }

    @Override
    public RestaurantMenuResponseDto getMenuByRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        List<MenuItems> items = menuItemRepository.findByRestaurantAndIsAvailableTrue(restaurant);

        Map<String, List<MenuItemResponseDto>> groupedItems = items.stream()
                .map(item -> modelMapper.map(item, MenuItemResponseDto.class))
                .collect(Collectors.groupingBy(MenuItemResponseDto::getCategory));

        List<MenuCategoryResponseDto> categories =
                groupedItems.entrySet().stream()
                        .map(entry -> MenuCategoryResponseDto.builder()
                                .category(entry.getKey())
                                .items(entry.getValue())
                                .build())
                        .toList();

        return RestaurantMenuResponseDto.builder()
                .categories(categories)
                .restaurant(modelMapper.map(restaurant, RestaurantSummaryDto.class))
                .build();
    }

    @Override
    public RestaurantOwnerProfileResponseDto getRestaurantProfile(Long userId) {

        Restaurant restaurant = getRestaurantForOwner(userId);

        long totalMenuItems =
                menuItemRepository.countByRestaurant(restaurant);

        long activeMenuItems =
                menuItemRepository.countByRestaurantAndIsAvailableTrue(restaurant);

        return RestaurantOwnerProfileResponseDto.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .isOpen(restaurant.getIsOpen())
                .rating(restaurant.getRating())
                .imageUrl(restaurant.getImageUrl())
                .address(restaurant.getAddress())
                .city(restaurant.getCity())
                .state(restaurant.getState())
                .zipcode(restaurant.getZipcode())
                .ownerId(restaurant.getOwnerId())
                .totalMenuItems((int) totalMenuItems)
                .activeMenuItems((int) activeMenuItems)
                .joinedAt(restaurant.getCreatedAt())
                .build();
    }

    @Override
    public RestaurantOwnerProfileResponseDto updateRestaurantProfile(Long userId) {
        Restaurant restaurant =  getRestaurantForOwner(userId);

        return mapToRestaurantOwnerProfileResponseDto(restaurant);
    }

    private RestaurantOwnerProfileResponseDto mapToRestaurantOwnerProfileResponseDto(Restaurant restaurant){
        return RestaurantOwnerProfileResponseDto.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .isOpen(restaurant.getIsOpen())
                .rating(restaurant.getRating())
                .imageUrl(restaurant.getImageUrl())
                .address(restaurant.getAddress())
                .city(restaurant.getCity())
                .state(restaurant.getState())
                .zipcode(restaurant.getZipcode())
                .joinedAt(restaurant.getCreatedAt())
                .build();
    }

    private Restaurant getRestaurantForOwner(Long userId) {
        return restaurantRepository.findByOwnerId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
    };

}
