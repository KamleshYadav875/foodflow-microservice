package com.foodflow.order_service.client;

import com.foodflow.order_service.dto.MenuItemInternalDto;
import com.foodflow.order_service.dto.RestaurantDetailResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface RestaurantServiceClient {

    @GetExchange("/restaurant/internal/{restaurantId}")
    RestaurantDetailResponse getRestaurantDetail(@PathVariable Long restaurantId);

    @GetExchange("/internal/menu-items/{menuItemId}")
    MenuItemInternalDto getInternalMenuItem(@PathVariable Long menuItemId);
}
