package com.foodflow.order_service.service.impl;


import com.foodflow.order_service.client.RestaurantServiceClient;
import com.foodflow.order_service.client.UserServiceClient;
import com.foodflow.order_service.dto.*;
import com.foodflow.order_service.entity.Cart;
import com.foodflow.order_service.entity.CartItem;
import com.foodflow.order_service.entity.Order;
import com.foodflow.order_service.entity.OrderItem;
import com.foodflow.order_service.enums.CancelReason;
import com.foodflow.order_service.enums.OrderStatus;
import com.foodflow.order_service.exceptions.BadRequestException;
import com.foodflow.order_service.exceptions.ResourceNotFoundException;
import com.foodflow.order_service.producer.OrderEventProducer;
import com.foodflow.order_service.repository.CartItemRepository;
import com.foodflow.order_service.repository.CartRepository;
import com.foodflow.order_service.repository.OrderItemRepository;
import com.foodflow.order_service.repository.OrderRepository;
import com.foodflow.order_service.service.OrderService;
import com.foodflow.order_service.util.OrderStatusValidator;
import foodflow.event.order.OrderCreatedEvent;
import foodflow.event.order.OrderReadyEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderEventProducer orderEventProducer;
    private final UserServiceClient userServiceClient;
    private final RestaurantServiceClient restaurantServiceClient;

    @Override
    @Transactional
    public OrderCheckoutResponseDto checkout(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);

        if(cart == null){
            throw new BadRequestException("Cart is empty");
        }

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        if(cartItems.isEmpty()){
            throw new BadRequestException("Cart is empty");
        }

        UserDetailResponse customer = userServiceClient.getUserDetails(userId);
        RestaurantDetailResponse restaurant = restaurantServiceClient.getRestaurantDetail(cart.getRestaurantId());
        log.info("Restaurant {}",restaurant);

        if(ObjectUtils.isEmpty(restaurant)){
            log.error("Resturant is empty");
            throw new BadRequestException("Restaurant is empty");
        }
        Order order = Order.builder()
                .customerId(customer.getId())
                .customerName(customer.getCustomerName())
                .deliveryAddress(customer.getDeliveryAddress())
                .deliveryLatitude(customer.getDeliveryLatitude())
                .deliveryLongitude(customer.getDeliveryLongitude())
                .restaurantId(restaurant.getRestaurantId())
                .restaurantName(restaurant.getRestaurantName())
                .restaurantAddress(restaurant.getRestaurantAddress())
                .restaurantLatitude(restaurant.getRestaurantLatitude())
                .restaurantLongitude(restaurant.getRestaurantLongitude())
                .totalItems(cart.getTotalItems())
                .totalAmount(cart.getTotalAmount())
                .status(OrderStatus.CREATED)
                .build();

        log.info("order {}", order);

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = cartItems.stream()
                .map(item ->
                    OrderItem.builder()
                        .order(savedOrder)
                        .menuItemId(item.getMenuItemId())
                        .name(item.getMenuItemName())
                        .price(item.getPrice())
                        .quantity(item.getQuantity())
                        .totalAmount(item.getTotalPrice()).build()
                ).toList();

        orderItemRepository.saveAll(orderItems);

        cartItemRepository.deleteByCart(cart);
        cartRepository.delete(cart);

        OrderCreatedEvent event = new OrderCreatedEvent(
                String.valueOf(order.getId()),
                userId,
                customer.getCustomerName(),
                customer.getCustomerPhone(),
                order.getRestaurantId(),
                order.getTotalAmount(),
                order.getCreatedAt()
        );
        orderEventProducer.publishOrderCreated(event);


        return OrderCheckoutResponseDto.builder()
                .orderId(savedOrder.getId())
                .totalAmount(savedOrder.getTotalAmount())
                .status(savedOrder.getStatus())
                .totalItems(savedOrder.getTotalItems())
                .createdAt(savedOrder.getCreatedAt())
                .paymentLink(savedOrder.getPaymentLink())
                .paymentId(savedOrder.getPaymentId())
                .build();
    }

    @Override
    public OrderUpdateResponseDto updateOrderStatus(Long orderId, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        OrderStatus currentStatus = order.getStatus();

        OrderStatusValidator.validateTransition(currentStatus, request.getStatus());

        if(request.getStatus() == OrderStatus.CANCELLED || request.getStatus() == OrderStatus.REJECTED) {
            order.setCancelledAt(LocalDateTime.now());
            order.setCancelReason(request.getCancelReason());
        }

        order.setStatus(request.getStatus());
        Order update = orderRepository.save(order);

        if(update.getStatus().equals(OrderStatus.READY)){
            OrderReadyEvent
                     event = new OrderReadyEvent(String.valueOf(update.getId()), update.getCustomerId(), update.getRestaurantId(),update.getTotalAmount(), update.getCreatedAt());
            orderEventProducer.publishOrderReady(event);
        }

        return OrderUpdateResponseDto.builder()
                .orderId(update.getId())
                .status(update.getStatus())
                .build();
    }

    @Override
    public PageResponse<UserOrderResponseDto> getOrdersByUser(Long userId, int page, int size) {

        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Order> ordersPage =
                orderRepository.findByCustomerIdOrderByCreatedAtDesc(userId, pageable);

        List<UserOrderResponseDto> content =
                ordersPage.getContent()
                        .stream()
                        .map(order -> UserOrderResponseDto.builder()
                                .orderId(order.getId())
                                .restaurantName(order.getRestaurantName())
                                .restaurantId(order.getRestaurantId())
                                .status(order.getStatus())
                                .totalAmount(order.getTotalAmount())
                                .totalItems(order.getTotalItems())
                                .createdAt(order.getCreatedAt())
                                .cancelReason(order.getCancelReason())
                                .cancelledAt(order.getCancelledAt())
                                .deliveredAt(order.getDeliveredAt())
//                                .deliveryPartnerName(order.getDeliveryPartnerName())
                                .deliveryPartnerId(order.getDeliveryPartnerId())
                                .build()
                        )
                        .toList();

        return PageResponse.<UserOrderResponseDto>builder()
                .content(content)
                .page(ordersPage.getNumber())
                .size(ordersPage.getSize())
                .totalElements(ordersPage.getTotalElements())
                .totalPages(ordersPage.getTotalPages())
                .last(ordersPage.isLast())
                .build();
    }

    @Override
    public PageResponse<OrderResponseDto> getOrderByRestaurant(Long restaurantId, int page, int size) {
//        Restaurant restaurant = restaurantQueryService.getRestaurantById(restaurantId)
//                .orElseThrow(() -> new ResourceNotFoundException(Constant.RESTAURANT_NOT_FOUND));

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Order> orderPage = orderRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId, pageable);

        List<OrderResponseDto> response = orderPage.getContent().stream()
                .map(order -> OrderResponseDto.builder()
                        .orderId(order.getId())
                        .totalAmount(order.getTotalAmount())
                        .totalItems(order.getTotalItems())
                        .createdAt(order.getCreatedAt())
                        .status(order.getStatus())
                        .build()).toList();

        return PageResponse.<OrderResponseDto>builder()
                .content(response)
                .page(orderPage.getNumber())
                .size(orderPage.getSize())
                .totalElements(orderPage.getTotalElements())
                .last(orderPage.isLast())
                .totalPages(orderPage.getTotalPages())
                .build();

    }

    @Override
    public OrderDetailResponse getUserOrderDetails(Long userId, Long orderId) {
//       User user = userQueryService.getUserById(userId)
//               .orElseThrow(() -> new ResourceNotFoundException(Constant.USER_NOT_FOUND));

       Order order = orderRepository.findById(orderId)
               .orElseThrow(() -> new ResourceNotFoundException("User not found"));

       if (!order.getCustomerId().equals(userId)) {
           throw new BadRequestException("You are not allowed to view this order");
       }

       List<OrderItem> orderItem = orderItemRepository.findByOrder(order);

       List<OrderItemResponse> itemResponseList = orderItem.stream().map(item -> OrderItemResponse.builder()
               .name(item.getName())
               .quantity(item.getQuantity())
               .price(item.getPrice())
               .totalAmount(item.getTotalAmount())
               .build()).toList();

       return OrderDetailResponse.builder()
               .orderId(order.getId())
               .status(order.getStatus())
               .orderedAt(order.getCreatedAt())
//               .restaurantName(order.getRestaurant().getName())
//               .partnerId(order.getDeliveryPartner() == null ? null : order.getDeliveryPartner().getId())
//               .partnerName(order.getDeliveryPartner() == null ? null : order.getDeliveryPartner().getUser().getName())
//               .partnerRating(order.getDeliveryPartner() == null ? null : order.getDeliveryPartner().getRating())
               .items(itemResponseList)
               .build();
    }

    @Override
    public UserOrderStatsResponse getUserOrderStatsResponse(Long userId) {
        long totalOrders = orderRepository.countByCustomerId(userId);
        long activeOrders = orderRepository.countActiveOrders(userId);
        long cancelledOrders = orderRepository.countByCustomerIdAndStatus(userId, OrderStatus.CANCELLED);

        return UserOrderStatsResponse.builder()
                .activeOrders(activeOrders)
                .cancelledOrders(cancelledOrders)
                .totalOrders(totalOrders)
                .build();
    }

    @Override
    public void cancelUserOrder(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if(!order.getCustomerId().equals(userId)){
            throw new BadRequestException("You are not allow to cancel the order");
        }

        OrderStatusValidator.validateTransition(order.getStatus(), OrderStatus.CANCELLED);

        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelledAt(LocalDateTime.now());
        order.setCancelReason(CancelReason.USER_REQUEST);
        orderRepository.save(order);
    }

    @Override
    public void deliveryPartnerAssigned(Long orderId, Long partnerId) {
        Order order = orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setStatus(OrderStatus.OUT_FOR_PICKUP);
        order.setDeliveryPartnerId(partnerId);
        order.setAssignedAt(LocalDateTime.now());

        orderRepository.save(order);
    }

    @Override
    public void orderPickedUp(Long orderId, Long partnerId) {
        Order order = orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setStatus(OrderStatus.PICKED_UP);
        order.setPickedUpAt(LocalDateTime.now());

        orderRepository.save(order);
    }

    @Override
    public void orderDelivered(Long orderId, Long partnerId) {
        Order order = orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setStatus(OrderStatus.DELIVERED);
        order.setDeliveredAt(LocalDateTime.now());

        orderRepository.save(order);
    }

    @Override
    public List<PartnerOrderDetail> getActiveOrdersForPartner(Long partnerId) {

        List<Order> orders = orderRepository.findByDeliveryPartnerIdAndStatusIn(
                partnerId,
                List.of(
                        OrderStatus.OUT_FOR_PICKUP,
                        OrderStatus.PICKED_UP
                )
        );
        if(CollectionUtils.isEmpty(orders)){
            return List.of();
        }

        return orders.stream().map(this::mapToPartnerOrderDetail).toList();
    }

    @Override
    public List<PartnerOrderDetail> getOrdersHistoryForPartner(Long partnerId) {
        List<Order> orders = orderRepository.findByDeliveryPartnerIdAndStatusIn(
                partnerId,
                List.of(
                        OrderStatus.DELIVERED
                )
        );
        if(CollectionUtils.isEmpty(orders)){
            return List.of();
        }

        return orders.stream().map(this::mapToPartnerOrderDetail).toList();
    }

    @Override
    public void updatePaymentDetails(Long orderId, String paymentId, String paymentLink) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow();

        order.setPaymentId(paymentId);
        order.setPaymentLink(paymentLink);

        orderRepository.save(order);
    }


    private PartnerOrderDetail mapToPartnerOrderDetail(Order order){
        return PartnerOrderDetail.builder()
                .orderId(order.getId())
                .orderStatus(order.getStatus().name())
                .totalAmount(order.getTotalAmount())
                .totalItems(order.getTotalItems())
                .orderedAt(order.getCreatedAt())
                .restaurantName(order.getRestaurantName())
                .restaurantAddress(order.getRestaurantAddress())
                .customerName(order.getCustomerName())
                .deliveryAddress(order.getDeliveryAddress())
                .build();
    }

}
