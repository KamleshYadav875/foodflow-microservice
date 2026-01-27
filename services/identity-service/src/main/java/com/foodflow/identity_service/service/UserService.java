package com.foodflow.identity_service.service;

import com.foodflow.identity_service.dto.*;
import com.foodflow.identity_service.entity.User;
import com.foodflow.identity_service.enums.UserRole;

import java.util.Optional;

public interface UserService {

   Optional<User> getUserByEmail(String email );

   User saveUser(User newUser);

   UserInternalResponseDto onBoardRestaurantAdmin(UserInternalRequestDto request);

    void addRole(Long userId, UserRole userRole);

}
