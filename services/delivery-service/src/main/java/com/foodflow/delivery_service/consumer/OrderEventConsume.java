package com.foodflow.delivery_service.consumer;

import com.foodflow.delivery_service.strategy.DeliveryAssignmentStrategy;
import foodflow.event.order.OrderReadyEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsume {

    private final DeliveryAssignmentStrategy deliveryAssignmentStrategy;

    @KafkaListener(topics = "order-ready", groupId = "delivery-service")
    public void handleOrderReady(OrderReadyEvent event){
        log.info("Consuming Order ready event");
        deliveryAssignmentStrategy.broadcast(Long.valueOf(event.getOrderId()));
    }
}
