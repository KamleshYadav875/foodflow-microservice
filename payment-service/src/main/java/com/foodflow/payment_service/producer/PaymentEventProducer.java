package com.foodflow.payment_service.producer;

import foodflow.event.payment.PaymentFailedEvent;
import foodflow.event.payment.PaymentLinkCreatedEvent;
import foodflow.event.payment.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishPaymentSuccess(PaymentSuccessEvent event) {
        kafkaTemplate.send(
                "payment-success",
                event.getOrderId().toString(),
                event);
    }

    public void publishPaymentFailedEvent(PaymentFailedEvent event) {
        kafkaTemplate.send(
                "payment-failed",
                event.getOrderId().toString(),
                event
        );
    }

    public void publishPaymentLinkCreated(PaymentLinkCreatedEvent event) {
        kafkaTemplate.send("payment-link-created", String.valueOf(event.getOrderId()), event);
    }
}
