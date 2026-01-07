package com.foodflow.identity_service.service;


import com.foodflow.identity_service.dto.JwtResponse;
import com.foodflow.identity_service.dto.LoginRequest;
import com.foodflow.identity_service.dto.UserCreateRequest;
import com.foodflow.identity_service.dto.UserRegisterResponse;

public interface AuthService {

    JwtResponse login(LoginRequest request);

    UserRegisterResponse registerUser(UserCreateRequest request);
}
