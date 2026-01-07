package com.foodflow.order_service.service;


import com.foodflow.order_service.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {

    OrderCheckoutResponseDto checkout(Long userId);

    OrderUpdateResponseDto updateOrderStatus(Long orderId, UpdateOrderStatusRequest request);

    PageResponse<UserOrderResponseDto> getOrdersByUser(Long userId, int page, int size);

    PageResponse<OrderResponseDto> getOrderByRestaurant(Long restaurantId, int page, int size);

    OrderDetailResponse getUserOrderDetails(Long userId, Long orderId);

    UserOrderStatsResponse getUserOrderStatsResponse(Long userId);

    void cancelUserOrder(Long id, Long orderId);

    void deliveryPartnerAssigned(Long orderId, Long partnerId);

    void orderPickedUp(Long orderId, Long partnerId);

    void orderDelivered(Long orderId, Long partnerId);

    List<PartnerOrderDetail> getActiveOrdersForPartner(Long partnerId);

    List<PartnerOrderDetail> getOrdersHistoryForPartner(Long partnerId);

    void updatePaymentDetails(Long orderId, String paymentId, String paymentLink);
}
