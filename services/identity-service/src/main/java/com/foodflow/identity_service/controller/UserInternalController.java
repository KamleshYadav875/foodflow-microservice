package com.foodflow.identity_service.controller;

import com.foodflow.identity_service.dto.InternalUserCreateRequest;
import com.foodflow.identity_service.dto.InternalUserResponse;
import com.foodflow.identity_service.dto.UserDetailResponse;
import com.foodflow.identity_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.GetExchange;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
@Slf4j
public class UserInternalController {

    private final UserService userService;

    @PostMapping(value = "/restaurant-admin",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<InternalUserResponse> onBoardRestaurantAdmin(@RequestBody InternalUserCreateRequest request){
        return  ResponseEntity.ok(userService.onBoardRestaurantAdmin(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailResponse> getUserDetails(@PathVariable Long id){
        log.info("Internal user request received for id={}", id);

        return ResponseEntity.ok(userService.getUserDetails(id));
    }
}
