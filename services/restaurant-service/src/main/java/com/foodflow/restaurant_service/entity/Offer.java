package com.foodflow.restaurant_service.entity;

import com.foodflow.restaurant_service.entity.enums.DiscountType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "offer",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "idx_offer_restaurant_id_code",
                        columnNames = {"restaurant_id", "code"}
                )
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;
    private String title;
    private String description;

    @Column(nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    private Integer discountValue;
    private BigDecimal minOrderAmount;
    private BigDecimal maxDiscount;
    private LocalDate validTill;
    private LocalDate validFrom;
    private Boolean isActive = true;
}
