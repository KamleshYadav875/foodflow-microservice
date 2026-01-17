package com.foodflow.delivery_service.service;

public interface DeliveryAssignmentService {

    void acceptOrder(Long orderId, Long userId);
}
