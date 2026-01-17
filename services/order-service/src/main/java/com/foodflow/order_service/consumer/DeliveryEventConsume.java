package com.foodflow.order_service.consumer;

import com.foodflow.order_service.service.OrderService;
import foodflow.event.delivery.DeliveryPartnerAssignedEvent;
import foodflow.event.delivery.OrderDeliveredEvent;
import foodflow.event.delivery.OrderPickedUpEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryEventConsume {

    private final OrderService orderService;

    @KafkaListener(topics = "delivery-partner-assigned", groupId = "order-service")
    public void handleDeliveryPartnerAssigned(DeliveryPartnerAssignedEvent event){
        orderService.deliveryPartnerAssigned(event.getOrderId(), event.getPartnerId());
    }

    @KafkaListener(topics = "order-picked-up", groupId = "order-service")
    public void handleOrderPickedUp(OrderPickedUpEvent event){
        orderService.orderPickedUp(event.getOrderId(), event.getPartnerId());
    }

    @KafkaListener(topics = "order-delivered", groupId = "order-service")
    public void handleOrderDelivered(OrderDeliveredEvent event){
        orderService.orderDelivered(event.getOrderId(), event.getPartnerId());
    }
}
