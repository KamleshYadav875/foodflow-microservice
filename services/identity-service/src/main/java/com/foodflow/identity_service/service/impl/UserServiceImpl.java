package com.foodflow.identity_service.service.impl;


import com.foodflow.identity_service.client.MediaServiceClient;
import com.foodflow.identity_service.client.OrderServiceClient;
import com.foodflow.identity_service.dto.*;
import com.foodflow.identity_service.entity.User;
import com.foodflow.identity_service.enums.UserRole;
import com.foodflow.identity_service.enums.UserStatus;
import com.foodflow.identity_service.exceptions.BadRequestException;
import com.foodflow.identity_service.exceptions.ResourceNotFoundException;
import com.foodflow.identity_service.exceptions.UsernameNotFoundException;
import com.foodflow.identity_service.producer.UserEventProducer;
import com.foodflow.identity_service.repository.UserRepository;
import com.foodflow.identity_service.service.UserService;
import com.foodflow.identity_service.util.SecurityUtils;
import foodflow.event.identity.DeliveryRoleAssignedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
    public UserProfileResponseDto getMyProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserOrderStatsResponse orderStatsResponse = orderServiceClient.getUserOrderStats(user.getId());
        return UserProfileResponseDto.builder()
                .name(user.getName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .address(user.getAddress())
                .city(user.getCity())
                .state(user.getState())
                .zipcode(user.getZipcode())
                .userId(user.getId())
                .totalOrders((int) orderStatsResponse.getTotalOrders())
                .cancelledOrders((int) orderStatsResponse.getCancelledOrders())
                .activeOrders((int) orderStatsResponse.getActiveOrders())
                .joinedAt(user.getCreatedAt())
                .roles(user.getRoles())
                .build();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User saveUser(User newUser) {
        return userRepository.save(newUser);
    }

    @Override
    public void updateUserImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new BadRequestException("Image file is required");
        }
        Long userId = SecurityUtils.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String newImageUrl = mediaServiceClient.uploadImage(image, "user");
        user.setProfileImageUrl(newImageUrl);

        userRepository.save(user);

    }

    @Override
    public UserProfileResponseDto updateProfile(UpdateUserProfileRequest request) {

        Long userId = SecurityUtils.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setName(request.getName());
        user.setAddress(request.getAddress());
        user.setCity(request.getCity());
        user.setState(request.getState());
        user.setZipcode(request.getZipcode());

        User updatedUser = userRepository.save(user);

        UserOrderStatsResponse orderStatsResponse = orderServiceClient.getUserOrderStats(user.getId());
        return UserProfileResponseDto.builder()
                .userId(updatedUser.getId())
                .name(updatedUser.getName())
                .phone(updatedUser.getPhone())
                .email(updatedUser.getEmail())
                .profileImageUrl(updatedUser.getProfileImageUrl())
                .address(updatedUser.getAddress())
                .city(updatedUser.getCity())
                .state(updatedUser.getState())
                .zipcode(updatedUser.getZipcode())
                .totalOrders((int) orderStatsResponse.getTotalOrders())
                .cancelledOrders((int) orderStatsResponse.getCancelledOrders())
                .activeOrders((int) orderStatsResponse.getActiveOrders())
                .roles(updatedUser.getRoles())
                .joinedAt(updatedUser.getCreatedAt())
                .build();
    }


    @Override
    public InternalUserResponse onBoardRestaurantAdmin(InternalUserCreateRequest request) {
        User user = userRepository.findByPhone(request.getPhone()).orElse(null);

        if(!ObjectUtils.isEmpty(user)){
            if(!user.getRoles().contains(UserRole.RESTAURANT)) {
                user.getRoles().add(UserRole.RESTAURANT);
                user = userRepository.save(user);
            }

            return InternalUserResponse.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .build();
        }
        String tempPassword = UUID.randomUUID().toString();
        log.info("Temp Password: {}", tempPassword);
        user = User.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(tempPassword))
                .roles(Set.of(UserRole.USER, UserRole.RESTAURANT))
                .status(UserStatus.ACTIVE)
                .build();
        User savedUser = userRepository.save(user);

        // TODO: send password reset / OTP

        return InternalUserResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .build();
    }

    @Override
    public void addRole(Long userId, UserRole userRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.getRoles().add(userRole);
        userRepository.save(user);

        DeliveryRoleAssignedEvent event = new DeliveryRoleAssignedEvent(userId, user.getName());
        userEventProducer.publishDeliveryRoleAssigned(event);
    }

    @Override
    public UserDetailResponse getUserDetails(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return UserDetailResponse.builder()
                .id(user.getId())
                .customerName(user.getName())
                .customerPhone(user.getPhone())
                .deliveryAddress(user.getAddress())
                .deliveryLatitude((double) 0)
                .deliveryLongitude((double) 0)
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByPhone(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
