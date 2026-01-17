package com.foodflow.order_service.util;


import com.foodflow.order_service.enums.OrderStatus;
import com.foodflow.order_service.exceptions.BadRequestException;

import java.util.Map;
import java.util.Set;

public class OrderStatusValidator {

    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED_TRANSITIONS =
                Map.of(
                        OrderStatus.CREATED, Set.of(
                                OrderStatus.CANCELLED,
                                OrderStatus.PLACED

                        ),
                        OrderStatus.PLACED, Set.of(OrderStatus.ACCEPTED, OrderStatus.CANCELLED),
                        OrderStatus.ACCEPTED, Set.of(OrderStatus.PREPARING),
                        OrderStatus.PREPARING, Set.of(OrderStatus.READY),
                        OrderStatus.READY, Set.of(OrderStatus.OUT_FOR_PICKUP),
                        OrderStatus.OUT_FOR_PICKUP, Set.of(OrderStatus.PICKED_UP),
                        OrderStatus.PICKED_UP, Set.of(OrderStatus.DELIVERED)
                );

    public static void validateTransition(OrderStatus current, OrderStatus next){

        if (current.isTerminal()) {
            throw new BadRequestException("Order already completed");
        }

        Set<OrderStatus> allowed = ALLOWED_TRANSITIONS.getOrDefault(current, Set.of());

        if(!allowed.contains(next)){
            throw new BadRequestException("Invalid order status transition from "+current +" to "+next);
        }
    }
}
