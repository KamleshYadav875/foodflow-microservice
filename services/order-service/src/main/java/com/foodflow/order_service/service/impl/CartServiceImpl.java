package com.foodflow.order_service.service.impl;

import com.foodflow.order_service.client.RestaurantServiceClient;
import com.foodflow.order_service.dto.*;
import com.foodflow.order_service.entity.Cart;
import com.foodflow.order_service.entity.CartItem;
import com.foodflow.order_service.exceptions.BadRequestException;
import com.foodflow.order_service.exceptions.ResourceNotFoundException;
import com.foodflow.order_service.repository.CartItemRepository;
import com.foodflow.order_service.repository.CartRepository;
import com.foodflow.order_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final ModelMapper modelMapper;
    private final CartRepository cartRepository;
    private final RestaurantServiceClient restaurantServiceClient;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public AddToCartResponse addItem(Long userId, AddToCartRequest request) {

        MenuItemInternalDto menuItem;
        try {
            menuItem = restaurantServiceClient
                    .getInternalMenuItem(request.getMenuItemId());
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResourceNotFoundException("Menu item not found");
        } catch (HttpClientErrorException ex) {
            throw new BadRequestException("Invalid menu item request");
        }

        if (request.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than zero");
        }

        if(Boolean.FALSE.equals(menuItem.getIsAvailable()))
            throw new BadRequestException("Menu item is not available");

        Cart cart = cartRepository.findByUserId(userId);

        if (cart != null &&
                !cart.getRestaurantId().equals(menuItem.getRestaurantId())) {
            throw new BadRequestException(
                    "Cart already contains items from another restaurant"
            );
        }

        if(ObjectUtils.isEmpty(cart)) {
            cart = Cart.builder()
                    .restaurantId(menuItem.getRestaurantId())
                    .userId(userId)
                    .totalItems(0)
                    .totalAmount(BigDecimal.ZERO)
                    .build();
            cart = cartRepository.save(cart);
        }

        CartItem cartItem = cartItemRepository
                .findByCartAndMenuItemId(cart, menuItem.getId())
                .orElse(null);

        BigDecimal itemTotal =
                menuItem.getPrice()
                        .multiply(BigDecimal.valueOf(request.getQuantity()));

        if (cartItem == null) {
            cartItem = CartItem.builder()
                    .cart(cart)
                    .menuItemId(menuItem.getId())
                    .price(menuItem.getPrice())
                    .menuItemName(menuItem.getMenuItemName())
                    .quantity(request.getQuantity())
                    .totalPrice(itemTotal)
                    .build();
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
            cartItem.setTotalPrice(
                    cartItem.getPrice().multiply(
                            BigDecimal.valueOf(cartItem.getQuantity()))
            );
        }
        cartItemRepository.save(cartItem);

        cart.setTotalItems(cart.getTotalItems() + request.getQuantity());
        cart.setTotalAmount(cart.getTotalAmount().add(itemTotal));
        cartRepository.save(cart);

        return AddToCartResponse.builder()
                .cartId(cart.getId())
                .restaurantId(cart.getRestaurantId())
                .totalAmount(cart.getTotalAmount())
                .totalItems(cart.getTotalItems())
                .build();

    }

    private CartResponseDto buildCartResponse(Cart cart) {
        List<CartItem> items = cartItemRepository.findByCart(cart);

        return CartResponseDto.builder()
                .cartId(cart.getId())
                .restaurantId(cart.getRestaurantId())
                .totalAmount(cart.getTotalAmount())
                .totalItems(cart.getTotalItems())
                .items(
                        items.stream()
                                .map(item -> modelMapper.map(item, CartItemResponseDto.class))
                                .toList()
                )
                .build();
    }

    @Override
    public CartResponseDto getCartByUser(Long userId) {

        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart is empty");
        }

        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponseDto updateItem(Long cartItemId, Integer quantity) {

        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than zero");
        }

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found in cart"));

        Cart cart = cartItem.getCart();

        int oldQuantity = cartItem.getQuantity();
        BigDecimal oldTotal = cartItem.getTotalPrice();

        cartItem.setQuantity(quantity);

        BigDecimal itemTotal = cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        cartItem.setTotalPrice(itemTotal);

        cartItemRepository.save(cartItem);

        cart.setTotalItems(cart.getTotalItems() - oldQuantity + quantity);
        cart.setTotalAmount(cart.getTotalAmount().subtract(oldTotal).add(itemTotal));
        cartRepository.save(cart);

        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public void removeItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found in cart"));

        Cart cart = cartItem.getCart();

        cart.setTotalItems(cart.getTotalItems() - cartItem.getQuantity());
        cart.setTotalAmount(cart.getTotalAmount().subtract(cartItem.getTotalPrice()));

        cartItemRepository.delete(cartItem);

        if (cart.getTotalItems() <= 0) {
            cartRepository.delete(cart);
        } else {
            cartRepository.save(cart);
        }

    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        cartItemRepository.deleteByCart(cart);
        cartRepository.delete(cart);
    }
}
