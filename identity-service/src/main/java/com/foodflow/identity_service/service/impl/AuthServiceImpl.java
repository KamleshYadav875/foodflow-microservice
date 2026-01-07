package com.foodflow.identity_service.service.impl;


import com.foodflow.identity_service.dto.JwtResponse;
import com.foodflow.identity_service.dto.LoginRequest;
import com.foodflow.identity_service.dto.UserCreateRequest;
import com.foodflow.identity_service.dto.UserRegisterResponse;
import com.foodflow.identity_service.entity.User;
import com.foodflow.identity_service.enums.UserRole;
import com.foodflow.identity_service.enums.UserStatus;
import com.foodflow.identity_service.exceptions.BadRequestException;
import com.foodflow.identity_service.jwt.JwtService;
import com.foodflow.identity_service.repository.UserRepository;
import com.foodflow.identity_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Override
    public JwtResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getPhone(), request.getPassword()));

        User user = (User) authentication.getPrincipal();
        if(user == null){
            throw new BadRequestException("Phone number or Password is incorrect");
        }

        String token = jwtService.generateToken(user);
        log.info(user.getRoles().toString());
        return JwtResponse.builder()
                .token(token)
                .roles(user.getRoles())
                .build();
    }

    @Override
    public UserRegisterResponse registerUser(UserCreateRequest request) {

        if(userRepository.existsByPhone(request.getPhone())){
            throw new BadRequestException("User already exists");
        }

        User user = User.builder()
                .phone(request.getPhone())
                .name(request.getName())
                .roles(Set.of(UserRole.USER))
                .password(passwordEncoder.encode(request.getPassword()))
                .status(UserStatus.ACTIVE)
                .build();

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserRegisterResponse.class);
    }


}
