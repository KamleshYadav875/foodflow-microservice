package com.foodflow.payment_service.controller;

import com.foodflow.payment_service.dto.RazorpayWebhookEvent;
import com.foodflow.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments/webhook")
@RequiredArgsConstructor
public class PaymentWebhookController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Void> handle(@RequestBody RazorpayWebhookEvent event) {
        paymentService.handlePaymentSuccess(event);
        return ResponseEntity.ok().build();
    }
}
