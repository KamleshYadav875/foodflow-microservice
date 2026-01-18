package com.foodflow.identity_service.controller;

import com.foodflow.identity_service.dto.JwtResponse;
import com.foodflow.identity_service.dto.LoginRequest;
import com.foodflow.identity_service.dto.UserCreateRequest;
import com.foodflow.identity_service.dto.UserRegisterResponse;
import com.foodflow.identity_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> registerUser(@RequestBody UserCreateRequest request) {

        UserRegisterResponse userResponse = authService.registerUser(request);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    // /api/auth/refresh-token


    // /api/auth/logout

}
