package com.foodflow.order_service.client;

import com.foodflow.order_service.dto.UserDetailResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface UserServiceClient {

     @GetExchange("/internal/users/{id}")
     UserDetailResponse getUserDetails(@PathVariable Long id);
}
