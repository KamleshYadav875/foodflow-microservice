package com.foodflow.restaurant_service.controller;

import com.foodflow.restaurant_service.dto.*;
import com.foodflow.restaurant_service.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menu-item")
public class MenuItemOwnerController {

    private final MenuItemService menuItemService;

    // Create menu item
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MenuItemResponseDto> createMenuItem(@RequestHeader("X-USER-ID") Long userId, @RequestPart("menuitem") MenuItemRequestDto request, @RequestPart(value = "image", required = false)MultipartFile image){
        MenuItemResponseDto response = menuItemService.createMenuItem(userId, request, image);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Update menu item image
    @PatchMapping(value = "/{menuItemId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateMenuItemImage(@RequestHeader("X-USER-ID") Long userId, @PathVariable Long menuItemId, @RequestPart(value = "image", required = false)MultipartFile image){
       menuItemService.updateMenuItemImage(userId,menuItemId, image);
        return ResponseEntity.noContent().build();
    }

    //Update menu item
    @PutMapping()
    public ResponseEntity<MenuItemResponseDto> updateMenuItem(@RequestHeader("X-USER-ID") Long userId, @RequestBody UpdateMenuItemRequest request){
        MenuItemResponseDto response = menuItemService.updateMenuItem(userId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Enable / disable menu item
    @PutMapping("/{menuItemId}/availability")
    public ResponseEntity<Void> updateMenuItemAvailability(@RequestHeader("X-USER-ID") Long userId, @PathVariable Long menuItemId){
        menuItemService.updateMenuItemAvailability(userId,menuItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{menuItemId}")
    public ResponseEntity<Void> deleteMenuItem(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long menuItemId
    ) {
        menuItemService.deleteMenuItem(userId, menuItemId);
        return ResponseEntity.noContent().build();
    }

}
