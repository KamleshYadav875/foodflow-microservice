package com.foodflow.order_service.controller;

import com.foodflow.order_service.dto.CartPreviewDto;
import com.foodflow.order_service.dto.PartnerOrderDetail;
import com.foodflow.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/order")
public class OrderInternalController {

    private final OrderService orderService;

    @GetMapping("/partners/{partnerId}/orders/active")
    public ResponseEntity<List<PartnerOrderDetail>> getActiveOrdersForPartner(@PathVariable Long partnerId){
        return ResponseEntity.ok(orderService.getActiveOrdersForPartner(partnerId));
    }

    @GetMapping("/partners/{partnerId}/orders/history")
    public ResponseEntity<List<PartnerOrderDetail>> getOrdersHistoryForPartner(@PathVariable Long partnerId){
        return ResponseEntity.ok(orderService.getOrdersHistoryForPartner(partnerId));
    }

    @GetMapping("/cart/preview/{userId}")
    public ResponseEntity<CartPreviewDto> getMyCartPreview(@PathVariable Long userId){
        return ResponseEntity.ok(orderService.getMyCartPreview(userId));
    }
}
