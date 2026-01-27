package com.foodflow.identity_service.service;


import com.foodflow.identity_service.dto.*;

public interface AuthService {

    JwtResponse login(LoginRequest request);

    UserRegisterResponse registerUser(UserRegisterRequest request);

    JwtResponse loginOTP(LoginOTPRequest request);

    void sendOTP(SendOtpRequest request);

    RefreshTokenResponse refreshToken(RefreshTokenRequest request);

    void resetPassword(ResetPasswordRequest request);

    VerifyResetOtpResponse verifyResetOtp(VerifyResetOtpRequest request);

    void setNewPassword(SetNewPasswordRequest request);
}
