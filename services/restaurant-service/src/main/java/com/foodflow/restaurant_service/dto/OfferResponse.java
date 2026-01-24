package com.foodflow.restaurant_service.dto;

import com.foodflow.restaurant_service.entity.enums.DiscountType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class OfferResponse {
    private Long id;
    private String title;
    private String description;
    private String code;
    private DiscountType discountType;
    private Integer discountValue;
    private BigDecimal minOrderAmount;
    private BigDecimal maxDiscount;
    private LocalDate validTill;
    private LocalDate validFrom;
    private boolean isActive;
}
