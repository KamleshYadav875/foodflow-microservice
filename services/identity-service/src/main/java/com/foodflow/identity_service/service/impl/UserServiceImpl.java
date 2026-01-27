package com.foodflow.identity_service.service.impl;


import com.foodflow.identity_service.client.MediaServiceClient;
import com.foodflow.identity_service.client.OrderServiceClient;
import com.foodflow.identity_service.dto.*;
import com.foodflow.identity_service.entity.User;
import com.foodflow.identity_service.enums.UserRole;
import com.foodflow.identity_service.enums.UserStatus;
import com.foodflow.identity_service.exceptions.ResourceNotFoundException;
import com.foodflow.identity_service.exceptions.UsernameNotFoundException;
import com.foodflow.identity_service.producer.UserEventProducer;
import com.foodflow.identity_service.repository.UserRepository;
import com.foodflow.identity_service.service.UserService;
import foodflow.event.identity.DeliveryRoleAssignedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final OrderServiceClient orderServiceClient;

    private final UserRepository userRepository;

    private final MediaServiceClient mediaServiceClient;

    private final PasswordEncoder passwordEncoder;

    private final UserEventProducer userEventProducer;

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User saveUser(User newUser) {
        return userRepository.save(newUser);
    }



    @Override
    public UserInternalResponseDto onBoardRestaurantAdmin(UserInternalRequestDto request) {
        User user = userRepository.findByPhone(request.getPhone()).orElse(null);

        if(!ObjectUtils.isEmpty(user)){
            if(!user.getRoles().contains(UserRole.RESTAURANT)) {
                user.getRoles().add(UserRole.RESTAURANT);
                user = userRepository.save(user);
            }

            return UserInternalResponseDto.builder()
                    .id(user.getId())
                    .build();
        }

        user = User.builder()
                .phone(request.getPhone())
                .roles(Set.of(UserRole.USER, UserRole.RESTAURANT))
                .status(UserStatus.ACTIVE)
                .build();
        User savedUser = userRepository.save(user);

        return UserInternalResponseDto.builder()
                .id(savedUser.getId())
                .build();
    }

    @Override
    public void addRole(Long userId, UserRole userRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.getRoles().add(userRole);
        userRepository.save(user);

        DeliveryRoleAssignedEvent event = new DeliveryRoleAssignedEvent(userId, "");
        userEventProducer.publishDeliveryRoleAssigned(event);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByPhone(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
