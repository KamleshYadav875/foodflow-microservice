package com.foodflow.payment_service.service.impl;


import com.foodflow.payment_service.dto.RazorpayWebhookEvent;
import com.foodflow.payment_service.entity.Payment;
import com.foodflow.payment_service.enums.PaymentStatus;
import com.foodflow.payment_service.exceptions.BadRequestException;
import com.foodflow.payment_service.exceptions.ResourceNotFoundException;
import com.foodflow.payment_service.producer.PaymentEventProducer;
import com.foodflow.payment_service.repository.PaymentRepository;
import com.foodflow.payment_service.service.PaymentService;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import foodflow.event.order.OrderCreatedEvent;
import foodflow.event.payment.PaymentFailedEvent;
import foodflow.event.payment.PaymentLinkCreatedEvent;
import foodflow.event.payment.PaymentSuccessEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer producer;

    @Value("${razorpay.api.key}")
    private String razorpayKey;

    @Value("${razorpay.api.secret}")
    private String razorpaySecret;

    @Override
    @Transactional
    public void createPaymentLink(OrderCreatedEvent event) {

        Payment payment = Payment.builder()
                .orderId(Long.valueOf(event.getOrderId()))
                .amount(event.getAmount())
                .status(PaymentStatus.PENDING)
                .build();

        payment = paymentRepository.save(payment);
        try {
            RazorpayClient client =
                    new RazorpayClient(razorpayKey, razorpaySecret);

            JSONObject request = new JSONObject();
            request.put("amount", event.getAmount().multiply(BigDecimal.valueOf(100)));
            request.put("currency", "INR");
            request.put("reference_id", event.getOrderId().toString());
            request.put("description", "FoodFlow Order #" + event.getOrderId());

            JSONObject customer = new JSONObject();
            customer.put("name", event.getCustomerName());
            customer.put("contact",event.getCustomerPhone());
            request.put("customer", customer);

            request.put("callback_method", "get");

            PaymentLink link = client.paymentLink.create(request);
            payment.setGatewayOrderId(link.get("id"));
            payment.setPaymentId(link.get("id"));
            payment.setPaymentLink(link.get("short_url"));
            paymentRepository.save(payment);

            producer.publishPaymentLinkCreated(
                    new PaymentLinkCreatedEvent(
                            payment.getOrderId(),
                            payment.getPaymentId(),
                            payment.getPaymentLink()
                    )
            );
        }
        catch (RazorpayException ex){
            throw new BadRequestException(ex.toString());
        }
    }

    @Override
    public void handlePaymentSuccess(RazorpayWebhookEvent event) {

        if (!event.isPaymentLinkPaid()) return;

        Payment payment = paymentRepository
                .findByGatewayOrderId(event.getPaymentLinkId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (payment.getStatus() == PaymentStatus.SUCCESS) return;

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setGatewayPaymentId(event.getPaymentId());
        paymentRepository.save(payment);

        PaymentSuccessEvent paymentSuccessEvent = new PaymentSuccessEvent(
                payment.getOrderId(),
                payment.getId(),
                payment.getAmount(),
                "SUCCESS",
                payment.getCreatedAt()
                );

        producer.publishPaymentSuccess(paymentSuccessEvent);
    }

    @Override
    public void handlePaymentFailed(RazorpayWebhookEvent event) {

        if (!event.isPaymentFailed()) return;

        Payment payment = paymentRepository
                .findByGatewayOrderId(event.getPaymentLinkId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (payment.getStatus() == PaymentStatus.FAILED) return;

        payment.setStatus(PaymentStatus.FAILED);
        payment.setGatewayPaymentId(event.getPaymentId());
        paymentRepository.save(payment);

        PaymentFailedEvent paymentFailedEvent = new PaymentFailedEvent(
                payment.getOrderId(),
                payment.getId(),
                payment.getAmount(),
                "FAILED",
                payment.getCreatedAt()
        );

        producer.publishPaymentFailedEvent(paymentFailedEvent);
    }

}

