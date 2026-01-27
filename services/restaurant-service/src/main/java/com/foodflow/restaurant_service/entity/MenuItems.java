package com.foodflow.restaurant_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "menu_items",
        indexes = {
                @Index(
                        name = "idx_menuitem_category",
                        columnList = "category"
                ),
                @Index(
                        name = "idx_menuitem_restaurant",
                        columnList = "restaurant_id"
                ),
                @Index(
                        name = "idx_menuitem_restaurant_category",
                        columnList = "restaurant_id, category"
                ),
                @Index(name = "idx_menuitem_available", columnList = "is_available")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(nullable = false)
    private String name;

    private String description;

    private String category;

    private BigDecimal price;

    private Boolean isVeg = false;

    private Boolean isBestseller = false;

    private Boolean isAvailable = true;

    private String imageUrl;

    @CreationTimestamp
    private LocalDateTime created;
}
