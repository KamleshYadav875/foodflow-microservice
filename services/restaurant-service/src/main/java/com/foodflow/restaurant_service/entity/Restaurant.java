package com.foodflow.restaurant_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(
        name = "restaurants",
        indexes = {
                @Index(
                        name = "idx_restaurant_city",
                        columnList = "city"),
                @Index(
                        name = "idx_restaurant_city_open",
                        columnList = "city, is_open"
                ),
                @Index(
                        name = "idx_restaurant_city_rating",
                        columnList = "city, rating"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String restaurantName;

    @Column(unique = true)
    private String phone;

    private String email;

    private String description;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String ownerName;

    private Boolean isOpen = true;

    private Float rating;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    private String state;

    @Column(nullable = false)
    private String pincode;

    private String imageUrl;

    private LocalTime openingTime;

    private LocalTime closingTime;

    @Column(nullable = false)
    private String fssaiNumber;

    private String gstNumber;

    @Column(nullable = false)
    private String panNumber;

    @Column(nullable = false)
    private String bankAccountNumber;

    @Column(nullable = false)
    private String ifscCode;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
