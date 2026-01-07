package com.foodflow.order_service.repository;

import com.foodflow.order_service.entity.Cart;
import com.foodflow.order_service.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCart(Cart cart);

    Optional<CartItem> findByCartAndMenuItemId(Cart cart, Long menuItemId);

    void deleteByCart(Cart cart);
}
