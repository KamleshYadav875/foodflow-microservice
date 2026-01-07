package com.foodflow.restaurant_service.service;

import com.foodflow.restaurant_service.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MenuItemService {

    MenuItemResponseDto createMenuItem(Long userId, MenuItemRequestDto request, MultipartFile image);

    void updateMenuItemAvailability(Long userId, Long menuItemId);

    void updateMenuItemImage(Long UserId, Long menuItemId, MultipartFile image);

    MenuItemResponseDto updateMenuItem(Long userId, UpdateMenuItemRequest request);

    MenuItemInternalDto getInternalMenuItemById(Long id);

    void deleteMenuItem(Long userId, Long menuItemId);
}
