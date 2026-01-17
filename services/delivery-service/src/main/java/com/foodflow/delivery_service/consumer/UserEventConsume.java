package com.foodflow.delivery_service.consumer;

import com.foodflow.delivery_service.service.DeliveryPartnerService;
import foodflow.event.identity.DeliveryRoleAssignedEvent;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserEventConsume {

    private final DeliveryPartnerService deliveryPartnerService;

    @KafkaListener(topics = "delivery-role-assigned", groupId = "delivery-service")
    public void handlerDeliveryRoleAssigned(DeliveryRoleAssignedEvent event){
        deliveryPartnerService.activateDeliveryPartner(event);
    }
}
