package com.foodflow.restaurant_service.controller;

import com.foodflow.restaurant_service.dto.MenuItemInternalDto;
import com.foodflow.restaurant_service.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MenuItemInternalController {

    private final MenuItemService menuItemService;

    @GetMapping("/internal/menu-items/{id}")
    public ResponseEntity<MenuItemInternalDto> getInternalMenuItemById(@PathVariable Long id){
        return  ResponseEntity.ok(menuItemService.getInternalMenuItemById(id));
    }
}
