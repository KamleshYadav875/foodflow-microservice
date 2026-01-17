package com.foodflow.identity_service.client;

import com.foodflow.identity_service.dto.UserOrderStatsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "order-service", url = "http://order-service:8082")
public interface OrderServiceClient {

    @GetMapping("/api/order/user-stats/{userId}")
    UserOrderStatsResponse getUserOrderStats(@PathVariable Long userId);

    @PatchMapping("/api/order/user/cancel")
    void cancelOrder(@RequestParam("id") Long id, @RequestPart("orderId") Long orderId);
}
