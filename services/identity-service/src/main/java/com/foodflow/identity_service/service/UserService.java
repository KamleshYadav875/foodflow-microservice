package com.foodflow.identity_service.service;

import com.foodflow.identity_service.dto.*;
import com.foodflow.identity_service.entity.User;
import com.foodflow.identity_service.enums.UserRole;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {
   UserProfileResponseDto getMyProfile(Long userId);

   Optional<User> getUserByEmail(String email );

   User saveUser(User newUser);

   void updateUserImage(MultipartFile image);

   UserProfileResponseDto updateProfile(UpdateUserProfileRequest request);

   InternalUserResponse onBoardRestaurantAdmin(InternalUserCreateRequest request);

    void addRole(Long userId, UserRole userRole);

    UserDetailResponse getUserDetails(Long id);
}
