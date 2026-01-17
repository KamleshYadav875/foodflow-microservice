package com.foodflow.order_service.service;

import com.foodflow.order_service.dto.AddToCartRequest;
import com.foodflow.order_service.dto.AddToCartResponse;
import com.foodflow.order_service.dto.CartResponseDto;

public interface CartService {
    AddToCartResponse addItem(Long userId, AddToCartRequest request);

    CartResponseDto getCartByUser(Long userId);

    CartResponseDto updateItem(Long cartItemId, Integer quantity);

    void removeItem(Long cartItemId);

    void clearCart(Long userId);
}
