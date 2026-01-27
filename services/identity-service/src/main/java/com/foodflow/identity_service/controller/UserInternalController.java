package com.foodflow.identity_service.controller;

import com.foodflow.identity_service.dto.UserInternalRequestDto;
import com.foodflow.identity_service.dto.UserInternalResponseDto;
import com.foodflow.identity_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/internal")
@RequiredArgsConstructor
@Slf4j
public class UserInternalController {

    private final UserService userService;

    @PostMapping("/restaurant-admin")
    public ResponseEntity<UserInternalResponseDto> onBoardRestaurantAdmin(@RequestBody UserInternalRequestDto request){
        return new ResponseEntity<>(userService.onBoardRestaurantAdmin(request), HttpStatus.CREATED);
    }
}
