package com.foodflow.order_service.controller;

import com.foodflow.order_service.dto.*;
import com.foodflow.order_service.producer.OrderEventProducer;
import com.foodflow.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    private final OrderEventProducer orderEventProducer;

    @PostMapping("/checkout")
    public ResponseEntity<OrderCheckoutResponseDto> checkout(@RequestHeader("X-USER-ID") Long userId) {
        return new ResponseEntity<>(
                orderService.checkout(userId),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/me")
    public ResponseEntity<PageResponse<UserOrderResponseDto>> getUserOrders(@RequestHeader("X-USER-ID") Long userId, @RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "1") int size){
        return ResponseEntity.ok(
                orderService.getOrdersByUser(userId, page, size)
        );
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrderDetails(@RequestHeader("X-USER-ID") Long userId, @PathVariable Long orderId){
        return ResponseEntity.ok(
                orderService.getUserOrderDetails(userId, orderId)
        );
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<PageResponse<OrderResponseDto>> getRestaurantOrders(
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ){
        return ResponseEntity.ok(orderService.getOrderByRestaurant(restaurantId, page,size));
    }

    @GetMapping("/user-stats/{userId}")
    public ResponseEntity<UserOrderStatsResponse> getUserOrderStatsResponse(
            @PathVariable Long userId
    ){
        return ResponseEntity.ok(orderService.getUserOrderStatsResponse(userId));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderUpdateResponseDto> updateOrderStatus(@PathVariable Long orderId, @RequestBody UpdateOrderStatusRequest request){
        return ResponseEntity.ok(
                orderService.updateOrderStatus(
                        orderId,
                        request
                )
        );
    }

    @PatchMapping("/user/cancel")
    public ResponseEntity<Void> cancelOrderByUser(@RequestParam("id") Long userId, @RequestParam("orderId") Long orderId){
        orderService.cancelUserOrder(
                userId,
                orderId
        );
        return ResponseEntity.noContent().build();
    }

}
