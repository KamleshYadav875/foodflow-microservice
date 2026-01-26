package com.foodflow.restaurant_service.client;

import com.foodflow.restaurant_service.dto.owner.RestaurantOrderDashboardResponse;
import com.foodflow.restaurant_service.dto.owner.RestaurantOrdersDetailResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@HttpExchange
public interface OrderServiceClient {

    @PostExchange("/user/internal/restaurant/{id}/dashboard")
    List<RestaurantOrderDashboardResponse> getRestaurantDashboard(@PathVariable Long id);

    @PostExchange("/user/internal/restaurant/{id}/status")
    List<RestaurantOrdersDetailResponse> getRestaurantOrderByStatus(@PathVariable Long id, @RequestParam("status") String status);
}
