package com.foodflow.customer_service.client;

import com.foodflow.customer_service.dto.CartPreviewDto;
import com.foodflow.customer_service.dto.CustomerOrderStatsResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PatchExchange;

@HttpExchange
public interface OrderServiceClient {

    @GetExchange("/api/order/user-stats/{userId}")
    CustomerOrderStatsResponse getUserOrderStats(@PathVariable Long userId);

    @GetExchange("/internal/order/cart/preview/{userId}")
    CartPreviewDto getMyCartPreview(@PathVariable Long userId);

    @PatchExchange("/api/order/user/cancel")
    void cancelOrder(@RequestParam("id") Long id, @RequestPart("orderId") Long orderId);
}
