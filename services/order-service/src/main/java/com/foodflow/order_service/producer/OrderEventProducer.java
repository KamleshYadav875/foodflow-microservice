package com.foodflow.order_service.producer;

import foodflow.event.order.OrderCreatedEvent;
import foodflow.event.order.OrderReadyEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishOrderCreated(OrderCreatedEvent event){
        kafkaTemplate.send("order-created", event.getOrderId(), event);
    }

    public void publishOrderReady(OrderReadyEvent event){
        log.info("Publishing Order ready event {}", event);
        kafkaTemplate.send("order-ready", event.getOrderId(), event);
    }
}
