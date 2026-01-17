package com.foodflow.restaurant_service.service.impl;


import com.foodflow.restaurant_service.client.MediaServiceClient;
import com.foodflow.restaurant_service.dto.*;
import com.foodflow.restaurant_service.entity.MenuItems;
import com.foodflow.restaurant_service.entity.Restaurant;
import com.foodflow.restaurant_service.exceptions.BadRequestException;
import com.foodflow.restaurant_service.exceptions.ResourceNotFoundException;
import com.foodflow.restaurant_service.exceptions.UnauthenticatedException;
import com.foodflow.restaurant_service.repository.MenuItemRepository;
import com.foodflow.restaurant_service.repository.RestaurantRepository;
import com.foodflow.restaurant_service.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuItemServiceImpl implements MenuItemService {

    private final RestaurantRepository restaurantRepository;

    private final MenuItemRepository menuItemRepository;

    private final ModelMapper modelMapper;

    private final MediaServiceClient mediaServiceClient;

    @Override
    @Transactional
    public MenuItemResponseDto createMenuItem(Long userId, MenuItemRequestDto request, MultipartFile image) {
        Restaurant restaurant = restaurantRepository.findByOwnerId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found for owner"));

        if (request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Price must be greater than zero");
        }

        String imageUrl = null;
        try {
            if (image != null && !image.isEmpty()) {
                imageUrl = mediaServiceClient.uploadImage(image, "menuitem");
            }
        } catch (Exception ex) {
            log.error("Image upload failed", ex);
            throw new BadRequestException("Failed to upload menu item image");
        }

        MenuItems menuItems = MenuItems.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .isVeg(request.getIsVeg())
                .isAvailable(true)
                .category(request.getCategory())
                .imageUrl(imageUrl)
                .restaurant(restaurant)
                .build();

        MenuItems savedMenuItem = menuItemRepository.save(menuItems);
        return modelMapper.map(savedMenuItem, MenuItemResponseDto.class);
    }

    @Override
    public void updateMenuItemAvailability(Long userId, Long menuItemId) {
        MenuItems menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found"));

        Long ownerId = menuItem.getRestaurant().getOwnerId();
        if (!ownerId.equals(userId)) {
            throw new UnauthenticatedException("Not authorized to modify this menu item");
        }

        menuItem.setIsAvailable(!menuItem.getIsAvailable());
        menuItemRepository.save(menuItem);
    }

    @Override
    public void updateMenuItemImage(Long userId, Long menuItemId, MultipartFile image) {

        if (image == null || image.isEmpty()) {
            throw new BadRequestException("Image file is required");
        }

        MenuItems menuItems = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menuitem not found"));

        Long ownerId = menuItems.getRestaurant().getOwnerId();
        if (!ownerId.equals(userId)) {
            throw new UnauthenticatedException("Not authorized to modify this menu item");
        }

        String newImageUrl = mediaServiceClient.uploadImage(image, "menuitem");
        menuItems.setImageUrl(newImageUrl);

        menuItemRepository.save(menuItems);
    }

    @Override
    public MenuItemResponseDto updateMenuItem(Long userId, UpdateMenuItemRequest request) {
        MenuItems menuItems = menuItemRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found"));

        Long ownerId = menuItems.getRestaurant().getOwnerId();
        if (!ownerId.equals(userId)) {
            throw new UnauthenticatedException("Not authorized to modify this menu item");
        }

        menuItems.setName(request.getName());
        menuItems.setDescription(request.getDescription());
        menuItems.setCategory(request.getCategory());
        menuItems.setPrice(request.getPrice());
        menuItems.setIsAvailable(request.getIsAvailable());
        menuItems.setIsVeg(request.getIsVeg());

        MenuItems updatedMenuItem = menuItemRepository.save(menuItems);
        return  modelMapper.map(updatedMenuItem, MenuItemResponseDto.class);
    }

    @Override
    @Transactional
    public void deleteMenuItem(Long userId, Long menuItemId) {

        MenuItems menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        if (!menuItem.getRestaurant().getOwnerId().equals(userId)) {
            throw new UnauthenticatedException("Not authorized");
        }

        menuItemRepository.delete(menuItem);
    }


    ///////////////////////// INTERNAL COMMUNICATION ////////////

    @Override
    public MenuItemInternalDto getInternalMenuItemById(Long id) {
        MenuItems menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        return MenuItemInternalDto.builder()
                .id(menuItem.getId())
                .restaurantId(menuItem.getRestaurant().getId())
                .menuItemName(menuItem.getName())
                .price(menuItem.getPrice())
                .isAvailable(menuItem.getIsAvailable())
                .build();
    }

}
