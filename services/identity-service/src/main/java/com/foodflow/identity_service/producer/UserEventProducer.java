package com.foodflow.identity_service.producer;

import foodflow.event.identity.DeliveryRoleAssignedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishDeliveryRoleAssigned(DeliveryRoleAssignedEvent event){
        kafkaTemplate.send("delivery-role-assigned", String.valueOf(event.getUserId()), event);
    }
}
