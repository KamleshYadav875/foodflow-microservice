package com.foodflow.order_service.entity;

import com.foodflow.order_service.enums.CancelReason;
import com.foodflow.order_service.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long customerId;
    private String customerName;
    private String deliveryAddress;
    private Double deliveryLatitude;
    private Double deliveryLongitude;

    @Column(nullable = false)
    private Long restaurantId;
    private String restaurantName;
    private String restaurantAddress;
    private Double restaurantLatitude;
    private Double restaurantLongitude;

    private String paymentLink;
    private String paymentId;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    private BigDecimal totalAmount;
    private Integer totalItems;

    @Enumerated(EnumType.STRING)
    private CancelReason cancelReason;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    private Long deliveryPartnerId;

    private LocalDateTime assignedAt;
    private LocalDateTime pickedUpAt;
    private LocalDateTime deliveredAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
