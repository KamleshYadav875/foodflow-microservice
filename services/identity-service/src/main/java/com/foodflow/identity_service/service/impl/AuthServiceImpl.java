package com.foodflow.identity_service.service.impl;


import com.foodflow.identity_service.client.CustomerServiceClient;
import com.foodflow.identity_service.dto.*;
import com.foodflow.identity_service.entity.User;
import com.foodflow.identity_service.enums.UserRole;
import com.foodflow.identity_service.enums.UserStatus;
import com.foodflow.identity_service.exceptions.BadRequestException;
import com.foodflow.identity_service.exceptions.ResourceNotFoundException;
import com.foodflow.identity_service.jwt.JwtService;
import com.foodflow.identity_service.repository.UserRepository;
import com.foodflow.identity_service.service.AuthService;
import com.foodflow.identity_service.util.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final CustomerServiceClient customerServiceClient;
    private final StringRedisTemplate redisTemplate;
    private final OtpService otpService;

    @Override
    public JwtResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getPhone(), request.getPassword()));
        User user = (User) authentication.getPrincipal();
        if(user == null){
            throw new BadRequestException("Phone number or Password is incorrect");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .roles(user.getRoles())
                .build();
    }

    @Override
    @Transactional
    public UserRegisterResponse registerUser(UserRegisterRequest request) {

        if(userRepository.existsByPhone(request.getPhone())){
            throw new BadRequestException("User already exists");
        }

        User user = User.builder()
                .phone(request.getPhone())
                .roles(Set.of(UserRole.USER))
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .status(UserStatus.ACTIVE)
                .build();

        User savedUser = userRepository.save(user);

        AddNewCustomerRequest addNewCustomerRequest = AddNewCustomerRequest.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .name(request.getName())
                .phone(savedUser.getPhone())
                .build();

        customerServiceClient.addCustomer(addNewCustomerRequest);
        return modelMapper.map(savedUser, UserRegisterResponse.class);
    }

    @Override
    public JwtResponse loginOTP(LoginOTPRequest request) {

        String key = "otp:login" + request;


        if(otpService.validateOtp(key, request.getOtp())){
            throw new BadRequestException("Invalid or expired OTP");
        }

        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        otpService.consumeOtp(key);

        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .roles(user.getRoles())
                .build();
    }

    @Override
    public void sendOTP(SendOtpRequest request) {
        String otpKey = "otp:login"+request.getPhone();

        String cooldownKey = "otp:cooldown:login" + request.getPhone();

        if(Boolean.TRUE.equals(redisTemplate.hasKey(cooldownKey))){
            throw new BadRequestException("Please wait before requesting another OTP");
        }

        String otp = otpService.generateSecureOtp();

        // Send OTP (SMS / WhatsApp / Email)
        // smsService.send(phone, "Your OTP is " + otp);
        log.info("Login OTP for phone {} is {}", request.getPhone(), otp);

        otpService.setOtp(otpKey, otp);
        otpService.setCooldown(cooldownKey, "1");
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        Long userId = Long.parseLong(jwtService.extractClaims(request.getRefreshToken()).getSubject());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid"));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return RefreshTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {

        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new ResourceNotFoundException("No user registered with give phone number"));

        String otpKey = "otp:reset"+request.getPhone();
        String coolDownKey = "otp:cooldown:reset"+request.getPhone();

        if(Boolean.TRUE.equals(redisTemplate.opsForValue().get(coolDownKey))){
            throw new BadRequestException("Please wait before requesting another OTP");
        }

        String otp = otpService.generateSecureOtp();

        // smsService.send(phone, "Your OTP is " + otp);
        log.info("Reset OTP for phone {} is {}", request.getPhone(), otp);

        otpService.setOtp(otpKey, otp);
        otpService.setCooldown(coolDownKey, "1");
    }

    @Override
    public VerifyResetOtpResponse verifyResetOtp(VerifyResetOtpRequest request) {
        String otpKey = "otp:reset"+request.getPhone();
        boolean isValid = otpService.validateOtp(otpKey, request.getOtp());
        return VerifyResetOtpResponse.builder()
                .valid(isValid)
                .build();
    }

    @Override
    public void setNewPassword(SetNewPasswordRequest request) {
        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new ResourceNotFoundException("No user exist with entered phone"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }




}
