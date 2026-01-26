package com.foodflow.restaurant_service.service.impl;

import com.foodflow.restaurant_service.client.OrderServiceClient;
import com.foodflow.restaurant_service.dto.owner.RestaurantOrderDashboardResponse;
import com.foodflow.restaurant_service.dto.owner.RestaurantDashboardResponse;
import com.foodflow.restaurant_service.dto.owner.RestaurantOrdersDetailResponse;
import com.foodflow.restaurant_service.entity.Restaurant;
import com.foodflow.restaurant_service.entity.enums.OrderStatus;
import com.foodflow.restaurant_service.exceptions.ResourceNotFoundException;
import com.foodflow.restaurant_service.repository.RestaurantRepository;
import com.foodflow.restaurant_service.service.RestaurantOwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantOwnerServiceImpl implements RestaurantOwnerService {

    private final OrderServiceClient orderServiceClient;
    private final RestaurantRepository restaurantRepository;

    @Override
    public RestaurantDashboardResponse dashboard(Long userId) {

        Restaurant restaurant = restaurantRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(""));

//        List<RestaurantOrderDashboardResponse> restaurantOrderDashboardResponseList = orderServiceClient.getRestaurantDashboard(restaurant.getId());
        RestaurantOrderDashboardResponse order1 = RestaurantOrderDashboardResponse.builder()
                .orderId(1L)
                .items(2)
                .placedAt(LocalDate.now())
                .totalAmount(BigDecimal.valueOf(120))
                .status(OrderStatus.READY)
                .build();
        RestaurantOrderDashboardResponse order2 = RestaurantOrderDashboardResponse.builder()
                .orderId(2L)
                .items(2)
                .placedAt(LocalDate.now())
                .totalAmount(BigDecimal.valueOf(120))
                .status(OrderStatus.PLACED)
                .build();
        RestaurantOrderDashboardResponse order3 = RestaurantOrderDashboardResponse.builder()
                .orderId(3L)
                .items(5)
                .placedAt(LocalDate.now())
                .totalAmount(BigDecimal.valueOf(1210))
                .status(OrderStatus.PLACED)
                .build();
        RestaurantOrderDashboardResponse order4 = RestaurantOrderDashboardResponse.builder()
                .orderId(4L)
                .items(1)
                .placedAt(LocalDate.now())
                .totalAmount(BigDecimal.valueOf(420))
                .status(OrderStatus.ACCEPTED)
                .build();
        List<RestaurantOrderDashboardResponse> restaurantOrderDashboardResponseList = new ArrayList<>();
        restaurantOrderDashboardResponseList.add(order1);
        restaurantOrderDashboardResponseList.add(order2);
        restaurantOrderDashboardResponseList.add(order3);
        restaurantOrderDashboardResponseList.add(order4);
        Integer todayOrders = (int) restaurantOrderDashboardResponseList.stream()
                .filter(
                        order -> order.getPlacedAt().equals(LocalDate.now()))
                .count();

        Integer pendingOrders = (int) restaurantOrderDashboardResponseList.stream()
                .filter(
                        order -> order.getStatus().equals(OrderStatus.PLACED))
                .count();

        Integer preparingOrders = (int) restaurantOrderDashboardResponseList.stream()
                .filter(
                        order -> order.getStatus().equals(OrderStatus.ACCEPTED))
                .count();

        BigDecimal todayRevenue = restaurantOrderDashboardResponseList.stream()
                .filter(
                        order -> order.getPlacedAt().equals(LocalDate.now()))
                .map(RestaurantOrderDashboardResponse::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<RestaurantOrderDashboardResponse> newOrders = restaurantOrderDashboardResponseList.stream()
                .filter(order -> order.getStatus().equals(OrderStatus.PLACED))
                .toList();

        List<RestaurantOrderDashboardResponse> preparingOrdersList = restaurantOrderDashboardResponseList.stream()
                .filter(order ->
                        order.getStatus().equals(OrderStatus.PREPARING) ||
                        order.getStatus().equals(OrderStatus.ACCEPTED) ||
                        order.getStatus().equals(OrderStatus.READY)

                        )
                .toList();

        List<RestaurantOrderDashboardResponse> readyOrders = restaurantOrderDashboardResponseList.stream()
                .filter(order -> order.getStatus().equals(OrderStatus.READY))
                .toList();

        List<RestaurantOrderDashboardResponse> recentOrders = restaurantOrderDashboardResponseList.stream()
                .filter(order -> order.getPlacedAt().equals(LocalDate.now()))
                .toList();

        return RestaurantDashboardResponse.builder()
                .isOpen(restaurant.getIsOpen() == null ? null : restaurant.getIsOpen())
                .todayOrders(todayOrders)
                .todayRevenue(todayRevenue)
                .pendingOrders(pendingOrders)
                .preparingOrders(preparingOrders)
                .newOrders(newOrders)
                .preparingOrdersList(preparingOrdersList)
                .readyOrders(readyOrders)
                .recentOrders(recentOrders)
                .build();
    }

    @Override
    public List<RestaurantOrdersDetailResponse> getOrdersByStatus(Long userId, String status) {

        Restaurant restaurant = restaurantRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(""));

        List<RestaurantOrdersDetailResponse> orderByStatusList = orderServiceClient.getRestaurantOrderByStatus(restaurant.getId(), status);
        return orderByStatusList;
    }
}
