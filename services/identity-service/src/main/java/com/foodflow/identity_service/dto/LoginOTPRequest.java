package com.foodflow.identity_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginOTPRequest {
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$")
    private String phone;

    @NotBlank(message = "OTP is required")
    private String otp;
}
