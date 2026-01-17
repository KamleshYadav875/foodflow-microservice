package com.foodflow.delivery_service.controller;

import com.foodflow.delivery_service.dto.*;
import com.foodflow.delivery_service.service.DeliveryPartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/delivery/partners")
public class DeliveryPartnerController {

    private final DeliveryPartnerService deliveryPartnerService;

    @PostMapping("/register")
    public ResponseEntity<DeliveryPartnerResponseDto> register(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestBody RegisterDeliveryPartnerRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(deliveryPartnerService.register(userId, request));
    }

    @GetMapping("/me")
    public ResponseEntity<PartnerProfileResponseDto> getPartnerProfile(@RequestHeader("X-USER-ID") Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(deliveryPartnerService.getPartnerProfile(userId));
    }

    @GetMapping("/me/orders/current")
    public ResponseEntity<List<PartnerOrderDetail>> getActiveOrdersForPartner(@RequestHeader("X-USER-ID") Long userId) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(deliveryPartnerService.getActiveOrdersForPartner(userId));
    }

    @GetMapping("/orders/history")
    public ResponseEntity<List<PartnerOrderDetail>> getOrdersHistoryForPartner(@RequestHeader("X-USER-ID") Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(deliveryPartnerService.getOrdersHistoryForPartner(userId));
    }

    @PutMapping("/{partnerId}/availability")
    public ResponseEntity<DeliveryPartnerResponseDto> updateAvailability(@PathVariable Long partnerId, @RequestBody UpdateAvailabilityRequest request){
        return ResponseEntity.ok(
                deliveryPartnerService.updateAvailability(partnerId, request.getAvailability())
        );
    }

    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<Void> updateDeliveryStatus(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long orderId,
            @RequestBody UpdateDeliveryStatusRequest request
    ) {
        deliveryPartnerService.updateStatus(
                userId,
                orderId,
                request.getStatus()
        );
        return ResponseEntity.noContent().build();
    }
}
