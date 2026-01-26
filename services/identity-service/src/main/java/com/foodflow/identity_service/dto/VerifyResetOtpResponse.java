package com.foodflow.identity_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyResetOtpResponse {

    private boolean valid;
}
