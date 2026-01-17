package com.foodflow.identity_service.consumer;

import com.foodflow.identity_service.enums.UserRole;
import com.foodflow.identity_service.service.UserService;
import foodflow.event.delivery.DeliveryPartnerRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeliveryPartnerEventConsume {

    private final UserService userService;

    @KafkaListener(topics = "delivery-partner-registered", groupId = "identity-service")
    public void handleDeliveryPartnerRegistered(DeliveryPartnerRegisteredEvent event){
        log.info("Consuming delivery-partner-registered event {}", event);
        userService.addRole(event.getUserId(), UserRole.DELIVERY);
    }
}
