package com.foodflow.payment_service.consumer;

import com.foodflow.payment_service.service.PaymentService;
import foodflow.event.order.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsume {

    private final PaymentService paymentService;

    @KafkaListener(topics = "order-created", groupId = "payment-service")
    public void handleOrderCreated(OrderCreatedEvent event){
        log.info("Handle payment for order {}", event.getOrderId());
        paymentService.createPaymentLink(event);
    }
}
