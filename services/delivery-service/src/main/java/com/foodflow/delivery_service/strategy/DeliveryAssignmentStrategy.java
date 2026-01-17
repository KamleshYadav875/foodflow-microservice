package com.foodflow.delivery_service.strategy;

public interface DeliveryAssignmentStrategy  {

    void broadcast(Long orderId);
}
