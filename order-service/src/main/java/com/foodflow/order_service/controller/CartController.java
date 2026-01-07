package com.foodflow.order_service.controller;

import com.foodflow.order_service.dto.AddToCartRequest;
import com.foodflow.order_service.dto.AddToCartResponse;
import com.foodflow.order_service.dto.CartResponseDto;
import com.foodflow.order_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<AddToCartResponse> addItem(@RequestHeader("X-USER-ID") Long userId,
                                                     @RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(
                cartService.addItem(userId, request)
        );
    }

    @GetMapping()
    public ResponseEntity<CartResponseDto> getCart(@RequestHeader("X-USER-ID") Long userId) {
        return ResponseEntity.ok(
                cartService.getCartByUser(userId)
        );
    }

    @PatchMapping("/item/{cartItemId}")
    public ResponseEntity<CartResponseDto> updateItem(
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(
                cartService.updateItem(cartItemId, quantity)
        );
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<Void> removeItem(
            @PathVariable Long cartItemId) {
        cartService.removeItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("")
    public ResponseEntity<Void> clearCart(@RequestHeader("X-USER-ID") Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}