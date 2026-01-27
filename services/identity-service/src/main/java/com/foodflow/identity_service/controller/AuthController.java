package com.foodflow.identity_service.controller;

import com.foodflow.identity_service.dto.*;
import com.foodflow.identity_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/login-otp")
    public ResponseEntity<JwtResponse> loginOTP(@RequestBody LoginOTPRequest request){
        return ResponseEntity.ok(authService.loginOTP(request));
    }

    @PostMapping("/send-otp")
    public ResponseEntity<MessageResponse> sendOTP(@RequestBody SendOtpRequest request){
        authService.sendOTP(request);
        return ResponseEntity.ok(MessageResponse.builder()
                .message("OTP sent successfully")
                .build());
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> registerUser(@RequestBody UserRegisterRequest request) {

        UserRegisterResponse userResponse = authService.registerUser(request);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        RefreshTokenResponse refreshTokenResponse = authService.refreshToken(request);
        return new ResponseEntity<>(refreshTokenResponse, HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(MessageResponse.builder()
                .message("Password reset OTP sent")
                .build());
    }

    @PostMapping("/verify-reset-otp")
    public ResponseEntity<VerifyResetOtpResponse> verifyResetOtp(@RequestBody VerifyResetOtpRequest request) {
        return ResponseEntity.ok(authService.verifyResetOtp(request));
    }

    @PostMapping("/set-new-password")
    public ResponseEntity<MessageResponse> setNewPassword(@RequestBody SetNewPasswordRequest request) {
        authService.setNewPassword(request);
        return ResponseEntity.ok(MessageResponse.builder()
                .message("Password updated successfully")
                .build());
    }

}
