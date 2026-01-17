package com.foodflow.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentLinkResponseDto {

    private String paymentId;
    private String paymentUrl;
}
