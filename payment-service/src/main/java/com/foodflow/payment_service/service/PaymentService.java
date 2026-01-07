package com.foodflow.payment_service.service;


import com.foodflow.payment_service.dto.RazorpayWebhookEvent;
import foodflow.event.order.OrderCreatedEvent;

public interface PaymentService {

    void createPaymentLink(OrderCreatedEvent event);

    void handlePaymentSuccess(RazorpayWebhookEvent event);

    void handlePaymentFailed(RazorpayWebhookEvent event);
}

