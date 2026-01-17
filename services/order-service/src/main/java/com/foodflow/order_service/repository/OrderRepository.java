package com.foodflow.order_service.repository;

import com.foodflow.order_service.entity.Order;
import com.foodflow.order_service.enums.OrderStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface

OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
        SELECT o FROM Order o
        WHERE o.status = :status
        AND o.createdAt < :expiryTime
        """)
    List<Order> findExpiredOrders(OrderStatus status, LocalDateTime expiryTime);

    Page<Order> findByCustomerIdOrderByCreatedAtDesc(Long customerId, Pageable pageable);

    Page<Order> findByRestaurantIdOrderByCreatedAtDesc(Long restaurantId, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from Order o where o.id = :orderId")
    Optional<Order> findByIdForUpdate(Long orderId);

    long countByCustomerId(Long customerId);


    @Query("""
       select count(o)
       from Order o
       where o.deliveryPartnerId = :partnerId
       and o.status in ('OUT_FOR_PICKUP','PICKED_UP')
    """)
    long countByDeliveryPartnerIdActiveOrder(Long partnerId);

    long countByCustomerIdAndStatus(Long customerId, OrderStatus status);

    @Query("""
       select count(o)
       from Order o
       where o.customerId = :customerId
       and o.status not in ('DELIVERED','CANCELLED')
    """)
    long countActiveOrders(Long customerId);

    List<Order> findByDeliveryPartnerIdAndStatusIn(Long partnerId, List<OrderStatus> outForPickup);


}
