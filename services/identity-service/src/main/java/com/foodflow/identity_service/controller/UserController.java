package com.foodflow.identity_service.controller;


import com.foodflow.identity_service.dto.UpdateUserProfileRequest;
import com.foodflow.identity_service.dto.UserProfileResponseDto;
import com.foodflow.identity_service.service.UserService;
import com.foodflow.identity_service.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@PreAuthorize("hasRole('USER')")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponseDto> getMyProfile() {
        Long userId = SecurityUtils.getCurrentUserId();
        UserProfileResponseDto userProfileResponseDto = userService.getMyProfile(userId);
        return ResponseEntity.ok(userProfileResponseDto);
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileResponseDto> updateProfile(@RequestBody UpdateUserProfileRequest request){
        UserProfileResponseDto response = userService.updateProfile(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping(value = "/me/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateMenuItemImage(@RequestPart(value = "image", required = false) MultipartFile image){
        userService.updateUserImage(image);
        return ResponseEntity.noContent().build();
    }
}
