package com.foodflow.restaurant_service.service;

import com.foodflow.restaurant_service.dto.owner.RestaurantDashboardResponse;
import com.foodflow.restaurant_service.dto.owner.RestaurantOrdersDetailResponse;

import java.util.List;

public interface RestaurantOwnerService {

    RestaurantDashboardResponse dashboard(Long userId);

    List<RestaurantOrdersDetailResponse> getOrdersByStatus(Long userId, String status);
}
