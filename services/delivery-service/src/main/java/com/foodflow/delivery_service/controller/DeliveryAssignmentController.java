package com.foodflow.delivery_service.controller;

import com.foodflow.delivery_service.service.DeliveryAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery/orders")
@RequiredArgsConstructor
public class DeliveryAssignmentController {

    private final DeliveryAssignmentService assignmentService;

    @PostMapping("/{orderId}/accept")
    public ResponseEntity<Void> acceptOrder(@RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long orderId
    ) {
        assignmentService.acceptOrder(orderId, userId);
        return ResponseEntity.ok().build();
    }
}
