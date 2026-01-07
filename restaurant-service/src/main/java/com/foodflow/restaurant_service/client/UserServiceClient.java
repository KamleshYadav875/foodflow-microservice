package com.foodflow.restaurant_service.client;

import com.foodflow.restaurant_service.dto.UserInternalRequestDto;
import com.foodflow.restaurant_service.dto.UserInternalResponseDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface UserServiceClient {

    @PostExchange("/user/internal/onboardadmin")
    UserInternalResponseDto onboardRestaurantAdmin(@RequestBody UserInternalRequestDto request);
}
