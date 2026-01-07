package com.foodflow.order_service.consumer;

import com.foodflow.order_service.dto.UpdateOrderStatusRequest;
import com.foodflow.order_service.entity.Order;
import com.foodflow.order_service.enums.CancelReason;
import com.foodflow.order_service.enums.OrderStatus;
import com.foodflow.order_service.repository.OrderRepository;
import com.foodflow.order_service.service.OrderService;
import foodflow.event.payment.PaymentFailedEvent;
import foodflow.event.payment.PaymentLinkCreatedEvent;
import foodflow.event.payment.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @KafkaListener(topics = "payment-success", groupId = "order-service")
    public void handlePaymentSuccess(PaymentSuccessEvent event){
        Order order = orderRepository.findByIdForUpdate(event.getOrderId())
                .orElseThrow();

        if (order.getStatus() != OrderStatus.CREATED) return;

        order.setStatus(OrderStatus.PLACED);
        orderRepository.save(order);
    }

    @KafkaListener(topics = "payment-failed", groupId = "order-service")
    public void handlePaymentFailed(PaymentFailedEvent event){
        UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();
        request.setStatus(OrderStatus.CANCELLED);
        request.setCancelReason(CancelReason.PAYMENT_FAILED);
        orderService.updateOrderStatus(event.getOrderId(),request);
    }

    @KafkaListener(topics = "payment-link-created", groupId = "order-service")
    public void handlePaymentLinkCreatedEvent(PaymentLinkCreatedEvent event) {
        orderService.updatePaymentDetails(event.getOrderId(),event.getPaymentId(), event.getPaymentLink());
    }
}
