package com.foodflow.restaurant_service.service.impl;


import com.foodflow.restaurant_service.client.MediaServiceClient;
import com.foodflow.restaurant_service.client.UserServiceClient;
import com.foodflow.restaurant_service.dto.*;
import com.foodflow.restaurant_service.entity.Cuisines;
import com.foodflow.restaurant_service.entity.MenuItems;
import com.foodflow.restaurant_service.entity.Offer;
import com.foodflow.restaurant_service.entity.Restaurant;
import com.foodflow.restaurant_service.exceptions.BadRequestException;
import com.foodflow.restaurant_service.exceptions.ResourceNotFoundException;
import com.foodflow.restaurant_service.repository.CuisinesRepository;
import com.foodflow.restaurant_service.repository.MenuItemRepository;
import com.foodflow.restaurant_service.repository.OfferRepository;
import com.foodflow.restaurant_service.repository.RestaurantRepository;
import com.foodflow.restaurant_service.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private final OfferRepository offerRepository;
    private final CuisinesRepository cuisinesRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public RestaurantRegisterResponse registerNewRestaurant(RestaurantRegisterRequest request) {

        boolean existsByPhone = restaurantRepository.existsByPhone(request.getPhone());

        if(existsByPhone){
            throw new BadRequestException("Restaurant is already register with phone number");
        }

        UserInternalRequestDto userRequest = UserInternalRequestDto.builder()
                .phone(request.getPhone())
                .build();
        UserInternalResponseDto user = userServiceClient.onboardRestaurantAdmin(userRequest);

        Restaurant restaurant = Restaurant.builder()
                .userId(user.getId())
                .restaurantName(request.getRestaurantName())
                .description(request.getDescription())
                .address(request.getAddress())
                .city(request.getCity())
                .pincode(request.getPincode())
                .ownerName(request.getOwnerName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .openingTime(request.getOpeningTime())
                .closingTime(request.getClosingTime())
                .fssaiNumber(request.getFssaiNumber())
                .gstNumber(request.getGstNumber())
                .panNumber(request.getPanNumber())
                .bankAccountNumber(request.getBankAccountNumber())
                .ifscCode(request.getIfscCode())
                .build();

        restaurantRepository.save(restaurant);

        return RestaurantRegisterResponse.builder()
                .success(true)
                .message("Restaurant registered successfully")
                .build();
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
                .restaurantName(restaurant.getRestaurantName())
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
                .name(restaurant.getRestaurantName())
                .description(restaurant.getDescription())
                .isOpen(restaurant.getIsOpen())
                .rating(restaurant.getRating())
                .imageUrl(restaurant.getImageUrl())
                .address(restaurant.getAddress())
                .city(restaurant.getCity())
                .state(restaurant.getState())
                .zipcode(restaurant.getPincode())
                .ownerId(restaurant.getUserId())
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

    @Override
    public List<RestaurantCardDto> getRestaurants(String city) {
        List<Restaurant> restaurantList = restaurantRepository.findByCity(city);

        return restaurantList.stream().map(restaurant -> RestaurantCardDto.builder()
                .id(restaurant.getId())
                .name(restaurant.getRestaurantName())
                .description(restaurant.getDescription())
                .rating(restaurant.getRating())
                .imageUrl(restaurant.getImageUrl())
                .isOpen(restaurant.getIsOpen())
                .deliveryTime("15 min")
                .distance("2.0 km")
                .cuisine(cuisinesRepository.findByRestaurantId(restaurant.getId()).stream().map(Cuisines::getName).toList())
                .offers(offerRepository.findByRestaurantId(restaurant.getId()).stream().map(Offer::getTitle).toList())
                .build()
                ).toList();
    }

    private RestaurantOwnerProfileResponseDto mapToRestaurantOwnerProfileResponseDto(Restaurant restaurant){
        return RestaurantOwnerProfileResponseDto.builder()
                .id(restaurant.getId())
                .name(restaurant.getRestaurantName())
                .description(restaurant.getDescription())
                .isOpen(restaurant.getIsOpen())
                .rating(restaurant.getRating())
                .imageUrl(restaurant.getImageUrl())
                .address(restaurant.getAddress())
                .city(restaurant.getCity())
                .state(restaurant.getState())
                .zipcode(restaurant.getPincode())
                .joinedAt(restaurant.getCreatedAt())
                .build();
    }

    private Restaurant getRestaurantForOwner(Long userId) {
        return restaurantRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
    };

}
