package com.foodflow.delivery_service.producer;

import foodflow.event.delivery.DeliveryPartnerAssignedEvent;
import foodflow.event.delivery.DeliveryPartnerRegisteredEvent;
import foodflow.event.delivery.OrderDeliveredEvent;
import foodflow.event.delivery.OrderPickedUpEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeliveryEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishRegistered(DeliveryPartnerRegisteredEvent event) {
        log.info("Publishing delivery-partner-registered event {}", event);
        kafkaTemplate.send("delivery-partner-registered", String.valueOf(event.getDeliveryPartnerId()), event);
    }

    public void publishDeliveryPartnerAssigned(Long orderId, Long partnerId) {
        DeliveryPartnerAssignedEvent event = new DeliveryPartnerAssignedEvent(orderId, partnerId);
        log.info("Publishing delivery-partner-assigned event {}", event);
        kafkaTemplate.send("delivery-partner-assigned", event.getOrderId().toString(), event);
    }

    public void publishOrderPickedUp(Long orderId, Long partnerId) {
        OrderPickedUpEvent event = new OrderPickedUpEvent(orderId, partnerId);
        log.info("Publishing order-picked-up event {}", event);
        kafkaTemplate.send("order-picked-up", event.getOrderId().toString(), event);
    }

    public void publishOrderDelivered(Long orderId, Long partnerId) {
        OrderDeliveredEvent event = new OrderDeliveredEvent(orderId, partnerId);
        log.info("Publishing order-delivered event {}", event);
        kafkaTemplate.send("order-delivered", event.getOrderId().toString(), event);
    }
}
